<?php

namespace Magnet\Controller;

use Silex\Application;
use Silex\ControllerProviderInterface;
use Magnet\Model\UserDAO;
use Magnet\Model\PinDAO;

class PinControllerProvider implements ControllerProviderInterface {
	public function connect(Application $app)
    {
    	// creates a new controller based on the default route
        $controllers = $app['controllers_factory'];

        //Gets all pins of a user.
        $controllers->get('/{token}', function (Application $app, $token)  {
            $result = array();
            $status = 200;
            $userDAO = new UserDAO();
            $pinDAO = new PinDAO($userDAO->getConnection());
            $user = $userDAO->findByToken($token);

            if($user !== null) {
                $result = $pinDAO->findByUserId($user->getId());
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