<?php

namespace Magnet\Model;

use \JsonSerializable;

class User implements JsonSerializable {
	private $id;
	private $login;
	private $password;
	private $lastLatitude;
	private $lastLongitude;
	private $token;

	public function __construct($data = array()) {
		if(is_array($data)) {
			if(isset($data['id'])) {
				$this->id = $data['id'];
			}

			if(isset($data['login'])) {
				$this->setLogin($data['login']);
			}

			if(isset($data['password'])) {
				$this->setPassword($data['password']);
			}

			if(isset($data['lastLatitude'])) {
				$this->setLastLatitude($data['lastLatitude']);
			}

			if(isset($data['lastLongitude'])) {
				$this->setLastLongitude($data['lastLongitude']);
			}

			if(isset($data['token'])) {
				$this->setToken($data['token']);
			}
		}
	}

	public function getId() {
		return $this->id;
	}

	public function setId($id) {
		if(is_numeric($id) && $id > 0) {
			$this->id = $id;
		}
	}

	public function getLogin() {
		return $this->login;
	}

	public function setLogin($login) {
		if(is_string($login)) {
			$this->login = $login;
		}
	}

	public function getPassword() {
		return $this->password;
	}

	public function setPassword($password) {
		if(is_string($password)) {
			$this->password = $password;
		}
	}

	public function getLastLatitude() {
		return $this->lastLatitude;
	}

	public function setLastLatitude($lastLongitude) {
		if(is_numeric($lastLatitude)) {
			$this->lastLatitude = $lastLatitude;
		}
	}

	public function getLastLongitude() {
		return $this->lastLongitude;
	}

	public function setLastLongitude($lastLongitude) {
		if(is_numeric($lastLongitude)) {
			$this->lastLongitude = $lastLongitude;
		}
	}

	public function getToken() {
		return $this->token;
	}

	public function setToken($token) {
		$this->token = $token;
	}

	public function jsonSerialize() {
		return [
			'login' => $this->getLogin(),
			'last_latitude' => $this->getLastLatitude(),
			'last_longitude' => $this->getLastLongitude()
		];
	}
}