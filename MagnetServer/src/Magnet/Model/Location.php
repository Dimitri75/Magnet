<?php

namespace Magnet\Model;

use \JsonSerializable;

class Location implements JsonSerializable {
	private $latitude;
	private $longitude;

	public function __construct($data = array()) {
		if(is_array($data)) {
			if(isset($data['latitude'])) {
				$this->setLatitude($data['latitude']);
			}

			if(isset($data['longitude'])) {
				$this->setLongitude($data['longitude']);
			}
		}
	}

	public function getLatitude() {
		return $this->latitude;
	}

	public function setLatitude($latitude) {
		if(is_numeric($latitude)) {
			$this->latitude = $latitude;
		}
	}

	public function getLongitude() {
		return $this->longitude;
	}

	public function setLongitude($longitude) {
		if(is_numeric($longitude)) {
			$this->longitude = $longitude;
		}
	}

	public function jsonSerialize() {
		return [
			'latitude' => $this->getLatitude(),
			'longitude' => $this->getLongitude(),
		];
	}
}

?>