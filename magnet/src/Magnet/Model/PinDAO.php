<?php

namespace Magnet\Model;

class PinDAO extends DAO {
	public function __constrct(PDO $connection = null) {
		parent::__constrct($connection);
	}

	public function find($id);
	public function findAll();
	public function save($data);
	public function update($data);
	public function delete($data);
}

?>