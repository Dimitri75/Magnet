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

        $controllers->get('/', function (Application $app) {
        	$groupDAO = new GroupDAO();
        	$groups = $groupDAO->findAll();

            return $app->json($groups);
        });

        //Gets all groups of a user.
        $controllers->get('/{token}', function (Application $app, $token)  {
            $result = array();
            $status = 200;
            $userDAO = new UserDAO();
            $groupDAO = new GroupDAO($userDAO->getConnection());
            $user = $userDAO->findByToken($token);

            if($user !== null) {
                $result = $groupDAO->findByUserId($user->getId());
            }
            else {
                $result['message'] = 'Token not valid.';
                $status = 401;
            }

            return $app->json($result, $status);
        });

        //Creates a new group.
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

        //Add a user to a group.
        $controllers->post('/{id}/{token}', function(Request $request, $id, $token) use($app) {
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
                        if($groupUser === $user) {
                            $userInGroup = true;
                            break;
                        }
                    }

                    $newUser = $userDAO->find($request->get('id_user'));
                    $groupUsers[] = $newUser;
                    $group->setUsers($groupUsers);
                    $groupId = $groupDAO->save($group);
                    //TO DO : Add a test to check if a user is in the group before adding (cannot add a user to somebody else's group)
                    if($groupId !== null) {
                        $result['message'] = 'User added to the group';
                    }
                    else {
                        $result['message'] = 'Error while adding the user.';
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