<?php

namespace Magnet\Model;

use \JsonSerializable;

class User implements JsonSerializable {
	private $id;
	private $login;
	private $password;
	private $lastLatitude;
	private $lastLongitude;

	public function __construct($data = array()) {
		if(is_array($data)) {
			if(isset($data['id'])) {
				$this->id = $sdata['id'];
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

	public function jsonSerialize() {
		return [
			'id' => $this->getId(),
			'login' => $this->getLogin(),
			'password' => $this->getPassword(),
			'lastLatitude' => $this->getLastLatitude(),
			'lastLongitude' => $this->getLastLongitude()
		];
	}
}