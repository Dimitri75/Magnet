<?php

namespace Magnet\Controller;

use Silex\Application;
use Silex\ControllerProviderInterface;
use Magnet\Model\PinDAO;

class PinControllerProvider implements ControllerProviderInterface {
	public function connect(Application $app)
    {
    	// creates a new controller based on the default route
        $controllers = $app['controllers_factory'];

        $controllers->get('/', function (Application $app) {
        	$pinDAO = new PinDAO();
        	$pins = $PinDAO->findAll();

            return $app->json($pins);
        });

        $controllers->get('/{id}', function (Application $app, $id)  {
        	$pinDAO = new PinDAO();
        	$pin = $PinDAO->find($id);

            return $app->json($pin);
        });

        return $controllers;
    }
}

?>