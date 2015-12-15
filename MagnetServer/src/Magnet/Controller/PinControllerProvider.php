<?php

namespace Magnet\Controller;

use Silex\Application;
use Silex\ControllerProviderInterface;
use Symfony\Component\HttpFoundation\Request;
use Magnet\Model\Location;
use Magnet\Model\Group;
use Magnet\Model\Pin;
use Magnet\Model\UserDAO;
use Magnet\Model\GroupDAO;
use Magnet\Model\PinDAO;

class PinControllerProvider implements ControllerProviderInterface {
	public function connect(Application $app)
    {
    	// creates a new controller based on the default route
        $controllers = $app['controllers_factory'];

        /**
         * @api {get} /pin/:token Request Pins of the User.
         * @apiName GetPins
         * @apiGroup Pin
         *
         * @apiParam {String} token  Token of the User.
         *
         * @apiSuccess {Array} pins The pins of the User
         *
         * @apiError TokenNotValid The <code>token</code> given cannot authenticate the User.
         */
        $controllers->get('/{token}', function (Application $app, $token)  {
            $result = array();
            $status = 200;
            $userDAO = new UserDAO();
            $pinDAO = new PinDAO($userDAO->getConnection());
            $user = $userDAO->findByToken($token);

            if($user !== null) {
                $result['pins'] = $pinDAO->findByUserId($user->getId());
            }
            else {
                $result['message'] = 'Token not valid.';
                $status = 401;
            }

            return $app->json(array($result, $status));
        });

        /**
         * @api {post} /pin/:token Creates a new Pin.
         * @apiName PostPin
         * @apiGroup Pin
         *
         * @apiParam {String} token  Token of the User.
         * @apiParam {String} name   Name of the Pin.
         * @apiParam {String} description   Description of the Pin.
         * @apiParam {Location} location   Location of the Pin.
         * @apiParam {Datetime} creation_time   Creation Time of the Pin.
         * @apiParam {Datetime} deletion_time   Deletion Time of the Pin.
         * @apiParam {Integer} group_id   Id of the group associated with the Pin.
         *
         * @apiError TokenNotValid   The <code>token</code> given cannot authenticate the User.
         * @apiError ErrorWhileSaving The pin couldn't be saved.
         */
        $controllers->post('/{token}', function(Request $request, $token) use($app) {
            $result = array();
            $status = 200;
            $userDAO = new UserDAO();
            $user = $userDAO->findByToken($token); 

            if($user !== null) {
                $pinDAO = new PinDAO($userDAO->getConnection());
                $location = new Location(json_decode($request->get('location'), true));
                $pin = new Pin(array('name' => $request->get('name'), 'description' => $request->get('description'), 'location' => $location,
                    'creation_time' => date($request->get('creation_time')), 'deletion_time' => date($request->get('deletion_time')), 'creator' => $user,
                    'id_group' => $request->get('group_id')));
                $pinId = $pinDAO->save($pin);

                if($pinId !== null) {
                    $pin->setId($pinId);
                    $result = $pin;
                }
                else {
                    $result['message'] = 'Error while saving the pin.';
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