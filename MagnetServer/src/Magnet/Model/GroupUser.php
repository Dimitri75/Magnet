<?php

namespace Magnet\Model;

use \JsonSerializable;

class GroupUser implements JsonSerializable {
	private $id;
	private $login;
	private $location;
	private $lastActivity;

	public function __construct($data = array()) {
		if(is_array($data)) {
			if(isset($data['id'])) {
				$this->id = $data['id'];
			}

			if(isset($data['login'])) {
				$this->setLogin($data['login']);
			}

			if(isset($data['location']) && $data['location'] instanceof Location) {
				$location = $data['location'];
			}
			else {
				$location = new Location();
				if(isset($data['latitude'])) {
					$location->setLatitude($data['latitude']);
				}
				if(isset($data['longitude'])) {
					$location->setLongitude($data['longitude']);
				}
			}
			$this->setLocation($location);

			if(isset($data['last_activity'])) {
				$this->setLastActivity($data['last_activity']);
			}
			else {
				$this->setLastActivity("0000-00-00 00:00:00");
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

	public function getLocation() {
		return $this->location;
	}

	public function setLocation($location) {
		$this->location = $location;
	}

	public function getLastActivity() {
		return $this->lastActivity;
	}

	public function setLastActivity($lastActivity) {
		$this->lastActivity = $lastActivity;
	}

	public function jsonSerialize() {
		return [
			'id' => $this->getId(),
			'login' => $this->getLogin(),
			'location' => $this->getLocation(),
			'last_activity' => $this->getLastActivity(),
		];
	}
}