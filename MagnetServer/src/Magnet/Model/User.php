<?php

namespace Magnet\Model;

use \JsonSerializable;

class User implements JsonSerializable {
	private $id;
	private $login;
	private $password;
	private $location;
	private $visible;
	private $token;
	private $lastActivity;
	private $groups;

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

			$location = new Location();
			if(isset($data['latitude'])) {
				$location->setLatitude($data['latitude']);
			}
			if(isset($data['longitude'])) {
				$location->setLatitude($data['longitude']);
			}
			$this->setLocation($location);

			if(isset($data['visible'])) {
				$this->setVisible($data['visible']);
			}

			if(isset($data['token'])) {
				$this->setToken($data['token']);
			}

			if(isset($data['last_activity'])) {
				$this->setLastActivity($data['last_activity']);
			}

			$this->setGroups(array());
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

	public function getVisible() {
		return $this->visible;
	}

	public function setVisible($visible) {
		if(is_bool($visible)) {
			$this->visible = $visible;
		}
	}

	public function getLocation() {
		return $this->location;
	}

	public function setLocation($location) {
		$this->location = $location;
	}

	public function getToken() {
		return $this->token;
	}

	public function setToken($token) {
		$this->token = $token;
	}

	public function getLastActivity() {
		return $this->lastActivity;
	}

	public function setLastActivity($lastActivity) {
		$this->lastActivity = $lastActivity;
	}

	public function getGroups() {
		return $this->groups;
	}

	public function setGroups($groups) {
		$this->groups = $groups;
	}

	public function jsonSerialize() {
		return [
			'login' => $this->getLogin(),
			'location' => $this->getLocation(),
			'visible' => $this->getVisible(),
			'last_activity' => $this->getLastActivity(),
			'groups' => $this->getGroups()
		];
	}
}