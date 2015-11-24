<?php

namespace Magnet\Controller;

use Silex\Application;
use Silex\ControllerProviderInterface;
use Symfony\Component\HttpFoundation\Request;
use Magnet\Model\Group;
use Magnet\Model\GroupDAO;
use Magnet\Model\UserDAO;

class GroupControllerProvider implements ControllerProviderInterface {
	public function connect(Application $app)
    {
    	// creates a new controller based on the default route
        $controllers = $app['controllers_factory'];

        /**
         * @api {get} /group/:token Request the groups of the User
         * @apiName GetGroups
         * @apiGroup Group
         *
         * @apiParam {String} token  Token of the User.
         *
         * @apiSuccess {Array} groups The groups of the User
         *
         * @apiError TokenNotValid The <code>token</code> given cannot authenticate the User.
         */
        $controllers->get('/{token}', function (Application $app, $token)  {
            $result = array();
            $status = 200;
            $userDAO = new UserDAO();
            $groupDAO = new GroupDAO($userDAO->getConnection());
            $user = $userDAO->findByToken($token);

            if($user !== null) {
                $result['groups'] = $groupDAO->findByUserId($user->getId());
            }
            else {
                $result['message'] = 'Token not valid.';
                $status = 401;
            }

            return $app->json($result, $status);
        });

        /**
         * @api {post} /group/:token Creates a new Group.
         * @apiName PostGroup
         * @apiGroup Group
         *
         * @apiParam {String} token  Token of the User.
         * @apiParam {String} name   Name of the Group.
         *
         * @apiError TokenNotValid   The <code>token</code> given cannot authenticate the User.
         * @apiError ErrorWhileSaving The group couldn't be saved.
         */
        $controllers->post('/{token}', function(Request $request, $token) use($app) {
            $result = array();
            $status = 200;
            $userDAO = new UserDAO();
            $user = $userDAO->findByToken($token); 

            if($user !== null) {
                $groupDAO = new GroupDAO($userDAO->getConnection());
                $group = new Group(array('name' => $request->get('name'), 'creator' => $user, 'users' => array($user)));
                $groupId = $groupDAO->save($group);

                if($groupId !== null) {
                    $group->setId($groupId);
                    $result = $group;
                }
                else {
                    $result['message'] = 'Error while saving the group.';
                }
            }
            else {
                $result['message'] = 'Token not valid.';
                $status = 401;
            }

            return $app->json($result, $status);
        });

        /**
         * @api {post} /group/:id/user/:token Add a User to a Group
         * @apiName AddUserTpGroup
         * @apiGroup Group
         *
         * @apiParam {Integer} id       Id of the Group to update.
         * @apiParam {String}  token    Token of the User.
         * @apiParam {Integer} login    Login of the User to add.
         *
         * @apiError TokenNotValid    The <code>token</code> given cannot authenticate the User.
         * @apiError ErrorWhileAdding The user couldn't be added.
         * @apiError UserNotInGroup   Cannot add user to a group the user is not in.
         * @apiError GroupNotValid    The group doesn't exist.
         */
        $controllers->post('/{id}/user/{token}', function(Request $request, $id, $token) use($app) {
            $result = array();
            $status = 200;
            $userDAO = new UserDAO();
            $user = $userDAO->findByToken($token); 

            if($user !== null) {
                $groupDAO = new GroupDAO($userDAO->getConnection());
                $group = $groupDAO->find($id);
                
                if($group !== null) {
                    $groupUsers = $group->getUsers();
                    $userInGroup = false;

                    foreach($groupUsers as $groupUser) {
                        if($groupUser->getLogin() === $user->getLogin()) {
                            $userInGroup = true;
                            break;
                        }
                    }

                    if($userInGroup) {
                        $newUser = $userDAO->findByLogin($request->get('login'));

                        if($newUser !== null) {
                            $groupUsers[] = $newUser;
                            $group->setUsers($groupUsers);
                            $groupId = $groupDAO->save($group);
                            
                            if($groupId !== null) {
                                $result['message'] = 'User added to the group';
                            }
                            else {
                                $result['message'] = 'Error while adding the user.';
                                $status = 400;
                            }
                        }
                        else {
                            $result['message'] = 'User to add cannot be found.';
                            $status = 400;
                        }
                    }
                    else {
                        $result['message'] = 'Cannot add user in a group the user is not in.';
                        $status = 400;
                    }
                }
                else {
                    $result['message'] = 'Group not valid.';
                    $status = 400;
                }
            }
            else {
                $result['message'] = 'Token not valid.';
                $status = 401;
            }

            return $app->json($result, $status);
        });

        return $controllers;
    }
}

?>