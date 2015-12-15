<?php

namespace Magnet\Model;

use \JsonSerializable;

class Pin implements JsonSerializable {
	private $id;
	private $name;
	private $description;
	private $location;
	private $creationTime;
	private $deletionTime;
	private $creator;
	private $idGroup;

	public function __construct($data = array()) {
		if(is_array($data)) {
			if(isset($data['id'])) {
				$this->id = $data['id'];
			}

			if(isset($data['name'])) {
				$this->setName($data['name']);
			}

			if(isset($data['description'])) {
				$this->setDescription($data['description']);
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

			if(isset($data['creation_time'])) {
				$this->setCreationTime($data['creation_time']);
			}

			if(isset($data['deletion_time'])) {
				$this->setDeletionTime($data['deletion_time']);
			}

			if(isset($data['creator'])) {
				$this->setCreator($data['creator']);
			}

			if(isset($data['id_group'])) {
				$this->setIdGroup($data['id_group']);
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

	public function getDescription() {
		return $this->description;
	}

	public function setDescription($description) {
		if(is_string($description)) {
			$this->description = $description;
		}
	}

	public function getLocation() {
		return $this->location;
	}

	public function setLocation($location) {
		$this->location = $location;
	}

	public function getCreationTime() {
		return $this->creationTime;
	}

	public function setCreationTime($creationTime) {
		$this->creationTime = $creationTime;
	}

	public function getDeletionTime() {
		return $this->creationTime;
	}

	public function setDeletionTime($deletionTime) {
		$this->deletionTime = $deletionTime;
	}

	public function getCreator() {
		return $this->creator;
	}

	public function setCreator($creator) {
		if($creator instanceof GroupUser) {
			$this->creator = $creator;
		}
	}

	public function getIdGroup() {
		return $this->idGroup;
	}

	public function setIdGroup($idGroup) {
		if(is_numeric($idGroup)) {
			$this->idGroup = $idGroup;
		}
	}

	public function jsonSerialize() {
		return [
			'id' => $this->getId(),
			'name' => $this->getName(),
			'description' => $this->getDescription(),
			'location' => $this->getLocation(),
			'creation_time' => $this->getCreationTime(),
			'deletion_time' => $this->getDeletionTime(),
			'creator' => $this->getCreator(),
			'id_group' => $this->getIdGroup()
		];
	}
}