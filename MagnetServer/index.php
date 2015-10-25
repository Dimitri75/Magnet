<?php

$loader = require_once __DIR__.'/vendor/autoload.php';
$loader->add('Magnet', __DIR__.'/src');

use Magnet\Model\User;
use Magnet\Model\UserDAO;
use Magnet\Controller\UserControllerProvider;
use Magnet\Controller\GroupControllerProvider;
use Magnet\Controller\PinControllerProvider;

$app = new Silex\Application();

$app['debug'] = true;

$app->mount('/user', new UserControllerProvider());
$app->mount('/group', new GroupControllerProvider());
$app->mount('/pin', new PinControllerProvider());

$app->run();