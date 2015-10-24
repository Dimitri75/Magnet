<?php

namespace Magnet\Model;

use \PDO;

abstract class DAO {
	private $connection;

	public function __construct(PDO $connection = null)	{
		$this->connection = $connection;
		if ($this->connection === null) {
			include('dbconfig.php');
			$this->connection = new PDO(
				$type.':host='.$host.';port='.$port.';dbname='.$name,
				$user,
				$password
			);
			$this->connection->setAttribute(
				PDO::ATTR_ERRMODE,
				PDO::ERRMODE_EXCEPTION
			);
		}
	}

	public function getConnection() {
		return $this->connection;
	}

	public function setConnection(PDO $connection) {
		$this->connection = $connection;
	}

	public abstract function find($id);
	public abstract function findAll();
	public abstract function save($data);
	public abstract function update($data);
	public abstract function delete($data);
}

?>