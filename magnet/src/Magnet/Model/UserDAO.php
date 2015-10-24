<?php

namespace Magnet\Model;

class UserDAO extends DAO {
	public function __constrct(PDO $connection = null) {
		parent::__constrct($connection);
	}

	public function find($id) {
		$result = null;

		if(is_numeric($id) && $id > 0) {
			$parameters = array(':id' => $id);

			$stmt = $this->getConnection()->prepare('
				SELECT * FROM users WHERE id = :id
			');
			$stmt->execute($parameters);

			if($stmt->rowCount > 0) {
				$result = new User($stmt->fetch());
			}
		}

		return $result;
	}

	public function findAll() {
		$result = array();

		$stmt = $this->getConnection()->prepare('
			SELECT * FROM users WHERE id = :id ORDER BY login
		');
		$stmt->execute();

		foreach($stmt->fetchAll() as $row) {
			$result[] = new User($row);
		}

		return $result;
	}

	public function save($data) {
		$id = null;

		if($data !== null && $data instanceof User) {
			if($data->getId() !== null) {
				$id = $this->update($data);
			}
			else {
				$parameters = array('login' => $data->getLogin(), 'password' => $data->getPassword(),
					'last_latitude' => $data->getLastLatitude(), 'last_longitude' => $data->getLastLongitude());

				$stmt = $this->getConnection()->prepare('
					INSERT INTO user (login, password, last_latitude, last_longitude)
					VALUES (:login, :password, :last_latitude, :last_longitude)
				');
				$stmt->execute($parameters);

				$id = $this->getConnection()->lastInsertId();
			}
		}

		return $id;
	}

	public function update($data) {
		$id = null;

		if($data !== null && $data instanceof User) {
			$parameters = array('id' => $data->getId(), 'login' => $data-getLogin(), 'password' => $data->getPassword(),
					'last_latitude' => $data->getLastLatitude(), 'last_longitude' => $data->getLastLongitude());

			$stmt = $this->getConnection()->prepare('
				UPDATE user SET login = :login, password = :password, last_latitude = :last_latitude, last_longitude = :last_longitude
				WHERE id = :id
			');
			$stmt->execute($parameters);

			$id = $data['id'];
		}

		return $id;
	}

	public function delete($data) {
		$result = false;

		if($data !== null && $data instanceof User) {
			$parameters = array('id' => $dat->getId());

			$stmt = $this->getConnection()->prepare('
				DELETE FROM user WHERE id = :id
			');
			$result = $stmt->execute($parameters);
		}

		return $result;
	}
}

?>