<?php

namespace Magnet\Controller;

use Silex\Application;
use Silex\ControllerProviderInterface;
use Symfony\Component\HttpFoundation\Request;
use Magnet\Model\UserDAO;
use Magnet\Model\GroupDAO;
use Magnet\Model\User;
use Magnet\Model\Location;

class UserControllerProvider implements ControllerProviderInterface {
	public function connect(Application $app)
    {
    	// creates a new controller based on the default route
        $controllers = $app['controllers_factory'];


		$controllers->get('/connected', function(Request $request) use($app) {
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

			return $app->json($result, $status);
		});

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

        //Gets a authentication token for a user, allowing him to be recognized as logged in.
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

        //Creates a new user, using a login and a password. Used when a user signs up.
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
                	$location = new Location($request->get('location'));
                	$user->seLocation($location);
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