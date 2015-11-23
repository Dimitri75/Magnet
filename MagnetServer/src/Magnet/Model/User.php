<?php

namespace Magnet\Model;

class User extends GroupUser {
	private $password;
	private $visible;
	private $token;
	private $groups;

	public function __construct($data = array()) {
		parent::__construct($data);

		if(is_array($data)) {
			if(isset($data['password'])) {
				$this->setPassword($data['password']);
			}

			if(isset($data['visible'])) {
				$this->setVisible($data['visible']);
			}
			else {
				$this->setVisible(false);
			}

			if(isset($data['token'])) {
				$this->setToken($data['token']);
			}

			$this->setGroups(array());
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

	public function getToken() {
		return $this->token;
	}

	public function setToken($token) {
		$this->token = $token;
	}

	public function getGroups() {
		return $this->groups;
	}

	public function setGroups($groups) {
		$this->groups = $groups;
	}

	public function jsonSerialize() {
		return [
			'id' => $this->getId(),
			'login' => $this->getLogin(),
			'location' => $this->getLocation(),
			'visible' => $this->getVisible(),
			'last_activity' => $this->getLastActivity(),
			'groups' => $this->getGroups()
		];
	}
}