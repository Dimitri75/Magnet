<?php

namespace Magnet\Controller;

use Silex\Application;
use Silex\ControllerProviderInterface;
use Symfony\Component\HttpFoundation\Request;
use Magnet\Model\UserDAO;
use Magnet\Model\User;

class UserControllerProvider implements ControllerProviderInterface {
	public function connect(Application $app)
    {
    	// creates a new controller based on the default route
        $controllers = $app['controllers_factory'];

        //Gets a authentication token for a user, allowing him to be recognized as logged on.
        $controllers->get('/{login}/{password}', function(Request $request, $login, $password) use($app) {
			$result = array();
			$status = 200;

			$userDAO = new UserDAO();
			$user = $userDAO->findByLogin($login);

			if($user !== null && $password === $user->getPassword()) {
				$token = bin2hex(openssl_random_pseudo_bytes(32));
				$user->setToken($token);
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

        return $controllers;
    }
}

?>