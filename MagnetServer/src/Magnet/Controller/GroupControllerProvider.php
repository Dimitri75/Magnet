<?php

namespace Magnet\Controller;

use Silex\Application;
use Silex\ControllerProviderInterface;
use Magnet\Model\GroupDAO;

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

        $controllers->get('/{id}', function (Application $app, $id)  {
        	$groupDAO = new GroupDAO();
        	$group = $groupDAO->find($id);

            return $app->json($group);
        });

        $controllers->get('/user/{token}', function (Application $app, $token)  {
            $userDAO = new UserDAO();
            $groupDAO = new GroupDAO($userDAO->getConnection());
            
            

            return $app->json($group);
        });

        return $controllers;
    }
}

?>