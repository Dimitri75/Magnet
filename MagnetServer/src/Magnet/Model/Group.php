<?php

namespace Magnet\Model;

use \JsonSerializable;

class Group implements JsonSerializable {
	private $id;
	private $name;
	private $creator;
	private $users;
	private $pins;

	public function __construct($data = array()) {
		if(is_array($data)) {
			if(isset($data['id'])) {
				$this->id = $data['id'];
			}

			if(isset($data['name'])) {
				$this->setName($data['name']);
			}

			if(isset($data['creator'])) {
				$this->setCreator($data['creator']);
			}

			if(isset($data['users'])) {
				$this->setUsers($data['users']);
			}
			else {
				$this->setUsers(array());
			}

			if(isset($data['pins'])) {
				$this->setPins($data['pins']);
			}
			else {
				$this->setPins(array());
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

	public function getName() {
		return $this->name;
	}

	public function setName($name) {
		if(is_string($name)) {
			$this->name = $name;
		}
	}

	public function getCreator() {
		return $this->creator;
	}

	public function setCreator($creator) {
		if($creator instanceof GroupUser) {
			$this->creator = $creator;
		}
	}

	public function getUsers() {
		return $this->users;
	}

	public function setUsers($users) {
		if(is_array($users)) {
			$this->users = $users;
		}
	}

	public function getPins() {
		return $this->pins;
	}

	public function setPins($pins) {
		if(is_array($pins)) {
			$this->pins = $pins;
		}
	}

	public function jsonSerialize() {
		return [
			'id' => $this->getId(),
			'name' => $this->getName(),
			'creator' => $this->getCreator(),
			'users' => $this->getUsers(),
			'pins' => $this->getPins()
		];
	}
}