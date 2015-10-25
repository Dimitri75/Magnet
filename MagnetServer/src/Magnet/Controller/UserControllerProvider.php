<?php

namespace Magnet\Controller;

use Silex\Application;
use Silex\ControllerProviderInterface;
use Symfony\Component\HttpFoundation\Request;
use Magnet\Model\UserDAO;

class UserControllerProvider implements ControllerProviderInterface {
	public function connect(Application $app)
    {
    	// creates a new controller based on the default route
        $controllers = $app['controllers_factory'];

        $controllers->get('/', function (Application $app) {
        	$userDAO = new UserDAO();
        	$users = $userDAO->findAll();

            return $app->json($users);
        });

        $controllers->get('/{id}', function (Application $app, $id)  {
        	$userDAO = new UserDAO();
        	$user = $userDAO->find($id);

            return $app->json($user);
        });

        $controllers->post('/register', function(Request $request) use($app) {
			$response = array();
			$login = $request->get('login');
			$password= $request->get('password');

			$userDAO = new UserDAO();
			$user = $userDAO->findByLogin($login);

			if($user === null) {
				$user = new User(array('login' => $login, 'password' => $password));
				$id = $userDAO->save($user);

				if($id !== null) {
					$response['success'] = 'User created';
				}
				else {
					$response['error'] = 'Error while savig user';
				}
			}
			else {
				$response['error'] = 'Login already used';
			}

			return $app->json($response);
		});

		$controllers->post('/login', function(Request $request) use($app) {
			$response = array();
			$login = $request->get('login');
			$password= $request->get('password');

			$userDAO = new UserDAO();
			$user = $userDAO->findByLogin($login);

			if($user !== null && $password === $user->getPassword()) {
				$token = bin2hex(openssl_random_pseudo_bytes(32));
				$user->setToken($token);
				$userDAO->save($user);
				$response['token'] = $token;
			}
			else {
				$response['error'] = $login . ' ' . $password;
			}

			return $app->json($response);
		});

        return $controllers;
    }
}

?>