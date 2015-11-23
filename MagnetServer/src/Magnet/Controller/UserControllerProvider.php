<?php

namespace Magnet\Controller;

use Silex\Application;
use Silex\ControllerProviderInterface;
use Symfony\Component\HttpFoundation\Request;
use Magnet\Model\UserDAO;
use Magnet\Model\GroupDAO;
use Magnet\Model\GroupUserDAO;
use Magnet\Model\User;
use Magnet\Model\Location;

class UserControllerProvider implements ControllerProviderInterface {
	public function connect(Application $app)
    {
    	// creates a new controller based on the default route
        $controllers = $app['controllers_factory'];

        /**
		 * @api {get} /user/ Request All Connected Users
		 * @apiName GetConnectedUsers
		 * @apiGroup User
		 *
		 * @apiSuccess {Array} connected List of connected Users.
		 */
		$controllers->get('/', function(Request $request) use($app) {
			$result = array();
			$status = 200;

			$userDAO = new UserDAO();
			$users = $userDAO->findAll();
			$now = strtotime(date("Y-m-d H:i:s"));

			foreach($users as $user) {
				$lastActivity = strtotime($user->getLastActivity());
				if($user->getToken() !== null && (($now - $lastActivity) < 300)) {
					$result[] = $user;
				}
			}

			return $app->json(array('connected' => $result), $status);
		});

		/**
		 * @api {get} /user/:id Request data about the User using an id.
		 * @apiName GetUserInfo
		 * @apiGroup User
		 *
		 * @apiParam {String} login		Login of the User.
		 *
		 * @apiSuccess {Integer}  id 			The id of the User
		 * @apiSuccess {String}   login 		The login of the User
		 * @apiSuccess {Location} location  	The location of the User
		 * @apiSuccess {Datetime} last_activity Last time of activity of the User
		 *
		 * @apiError IdNotFound The <code>id</code> doesn't match for any User.
		 */
        $controllers->get('/{id}', function(Request $request, $id) use($app) {
			$result = array();
			$status = 200;

			$userDAO = new GroupUserDAO();
			$user = $userDAO->find($id);

			if($user !== null && $user->getVisible()) {
				$result = $user;
			}
			else {
				$result['message'] = 'User with given id cannot be found.';
				$status = 400;
			}

			return $app->json($result, $status);
		})
		->assert('id', '\d+');

        /**
		 * @api {get} /user/:login Request data about the User using a login.
		 * @apiName GetUserInfo
		 * @apiGroup User
		 *
		 * @apiParam {String} login		Login of the User.
		 *
		 * @apiSuccess {Integer}  id 			The id of the User
		 * @apiSuccess {String}   login 		The login of the User
		 * @apiSuccess {Location} location  	The location of the User
		 * @apiSuccess {Datetime} last_activity Last time of activity of the User
		 *
		 * @apiError LoginNotFound The <code>login</code> doesn't match for any User.
		 */
        $controllers->get('/{login}', function(Request $request, $login) use($app) {
			$result = array();
			$status = 200;

			$userDAO = new GroupUserDAO();
			$user = $userDAO->findByLogin($login);

			if($user !== null && $user->getVisible()) {
				$result = $user;
			}
			else {
				$result['message'] = 'User with given login cannot be found.';
				$status = 400;
			}

			return $app->json($result, $status);
		});

		/**
		 * @api {get} /user/:token Request User information
		 * @apiName GetUser
		 * @apiGroup User
		 *
		 * @apiParam {String} token  Token of the User.
		 *
		 * @apiSuccess {Integer}  id 			The id of the User
		 * @apiSuccess {String}   login 		The login of the User
		 * @apiSuccess {Location} location 		The location of the User
		 * @apiSuccess {Boolean}  visible 		If the User is seen as connected
		 * @apiSuccess {Datetime} last_activity Last time of activity of the User
		 * @apiSuccess {Array} 	  groups 		The groups of the User
		 *
		 * @apiError TokenNotValid The <code>token</code> given cannot authenticate the User.
		 */
        $controllers->get('/{token}', function(Request $request, $token) use($app) {
			$result = array();
            $status = 200;
            $userDAO = new UserDAO();
            $groupDAO = new GroupDAO($userDAO->getConnection());
            $user = $userDAO->findByToken($token);

            if($user !== null) {
            	$user->setGroups($groupDAO->findByUserId($user->getId()));
            	$result = $user;
            }
            else {
                $result['message'] = 'Token not valid.';
                $status = 401;
            }

            return $app->json($result, $status);
		});

        /**
		 * @api {get} /user/:login/:password Requests a token for the User
		 * @apiName GetUserToken
		 * @apiGroup User
		 *
		 * @apiParam {String} login		Login of the User.
		 * @apiParam {String} password  Password of the User.
		 *
		 * @apiSuccess {String} token The token to authenticate the User
		 *
		 * @apiError CredentialsNotValid The <code>login</code> or <code>password</code> given doesn't match for any User.
		 */
        $controllers->get('/{login}/{password}', function(Request $request, $login, $password) use($app) {
			$result = array();
			$status = 200;

			$userDAO = new UserDAO();
			$user = $userDAO->findByLogin($login);

			if($user !== null && $password === $user->getPassword()) {
				$token = bin2hex(openssl_random_pseudo_bytes(32));
				$user->setToken($token);
				$user->setLastActivity(date("Y-m-d H:i:s"));
				$userDAO->save($user);
				$result['token'] = $token;
			}
			else {
				$result['message'] = 'Login or password doesn\'t match.';
				$status = 400;
			}

			return $app->json($result, $status);
		});

        /**
		 * @api {post} /user/ Creates a new User
		 * @apiName PostUser
		 * @apiGroup User
		 *
		 * @apiParam {String} login		Login of the User.
		 * @apiParam {String} password  Password of the User.
		 *
		 * @apiError LoginUsed 			The <code>login</code> is already used by another User.
		 * @apiError LoginUsed 			The <code>login</code> is already used by another User.
		 * @apiError LoginUsed 			The <code>login</code> or <code>password</code> is empty User.
		 * @apiError ErrorWhileSaving 	The User couldn't be saved.
		 */
        $controllers->post('/', function(Request $request) use($app) {
			$result = array();
			$status = 200;
			$login = $request->get('login');
			$password= $request->get('password');
			
			if($login !== null && $password !== null) {
				$userDAO = new UserDAO();
				$user = $userDAO->findByLogin($login);

				if($user === null) {
					$user = new User(array('login' => $login, 'password' => $password));
					$id = $userDAO->save($user);

					if($id !== null) {
						$result = $user;
					}
					else {
						$result['message'] = 'Error while savig user.';
						$status = 400;
					}
				}
				else {
					$result['message'] = 'Login already used.';
					$status = 400;
				}
			}
			else {
				$result['message'] = 'Login or password empty.';
				$status = 400;
			}

			return $app->json($result, $status);
		});

        /**
		 * @api {put} /user/ Updates a User
		 * @apiName PutUser
		 * @apiGroup User
		 *
		 * @apiParam {String} [password]  Password of the User.
		 * @apiParam {String} [location]  Location of the User.
		 * @apiParam {String} [visible]  Visibility of the User.
		 *
		 * @apiError TokenNotValid The <code>token</code> given cannot authenticate the User.
		 * @apiError ErrorWhileSaving 	The User couldn't be saved.
		 */
		$controllers->put('/{token}', function(Request $request, $token) use($app) {
			$result = array();
            $status = 200;
            $userDAO = new UserDAO();
            $groupDAO = new GroupDAO($userDAO->getConnection());
            $user = $userDAO->findByToken($token);

            if($user !== null) {
                if($request->get('password') !== null) {
                	$user->setPassword($request->get('password'));
                }

                if($request->get('location') !== null) {
                	$location = new Location(json_decode($request->get('location'), true));
                	$user->setLocation($location);
                }

                if($request->get('visible') !== null) {
                	$user->setVisible($request->get('visible'));
                }

                $user->setLastActivity(date("Y-m-d H:i:s"));
                $id = $userDAO->save($user);

				if($id !== null) {
					$result = $user;
				}
				else {
					$result['message'] = 'Error while savig user.';
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