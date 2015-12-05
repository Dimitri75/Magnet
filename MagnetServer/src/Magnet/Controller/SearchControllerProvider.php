<?php

namespace Magnet\Controller;

use Silex\Application;
use Silex\ControllerProviderInterface;
use Symfony\Component\HttpFoundation\Request;
use Magnet\Model\GroupUserDAO;

class SearchControllerProvider implements ControllerProviderInterface {
	public function connect(Application $app)
    {
    	// creates a new controller based on the default route
        $controllers = $app['controllers_factory'];

        /**
		 * @api {get} /search/user/:login Search Users using a login or part of it.
		 * @apiName SearchUserByLogin
		 * @apiGroup Search
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
        $controllers->get('/user/{login}', function(Request $request, $login) use($app) {
			$result = array();
			$status = 200;
			$userDAO = new GroupUserDAO();
			$result['users'] = $userDAO->searchByLogin($login);

			return $app->json($result, $status);
		});
        
		return $controllers;
    }
}

?>