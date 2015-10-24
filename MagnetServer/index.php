<?php

$loader = require_once __DIR__.'/vendor/autoload.php';
$loader->add('Magnet', __DIR__.'/src');

use Magnet\Model\User;
use Magnet\Model\UserDAO;

$app = new Silex\Application();

$app['debug'] = true;

$app->get('', function () {
	$user = new User(array('login' => 'toto', 'password' => 'azerty'));
	$userDAO = new UserDAO();
	$id = $userDAO->save($user);
    return 'ID of the new user : ' . $id;
});

$app->run();