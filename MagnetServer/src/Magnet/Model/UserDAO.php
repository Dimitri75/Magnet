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
				SELECT * FROM user WHERE id = :id
			');
			$stmt->execute($parameters);

			if($stmt->rowCount() > 0) {
				$result = new User($stmt->fetch());
			}
		}

		return $result;
	}

	public function findByLogin($login) {
		$result = null;

		if(is_string($login)) {
			$parameters = array(':login' => $login);

			$stmt = $this->getConnection()->prepare('
				SELECT * FROM user WHERE login = :login
			');
			$stmt->execute($parameters);

			if($stmt->rowCount() > 0) {
				$result = new User($stmt->fetch());
			}
		}

		return $result;
	}

	public function findByToken($token) {
		$result = null;

		if(is_string($token)) {
			$parameters = array(':token' => $token);

			$stmt = $this->getConnection()->prepare('
				SELECT * FROM user WHERE token = :token
			');
			$stmt->execute($parameters);

			if($stmt->rowCount() > 0) {
				$result = new User($stmt->fetch());
			}
		}

		return $result;
	}

	public function findAll() {
		$result = array();

		$stmt = $this->getConnection()->prepare('
			SELECT * FROM user ORDER BY login
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
				$parameters = array(':login' => $data->getLogin(), ':password' => $data->getPassword(),
					':last_latitude' => $data->getLastLatitude(), ':last_longitude' => $data->getLastLongitude(),
					':visible' => $data->getVisible(), ':token' => $data->getToken());

				$stmt = $this->getConnection()->prepare('
					INSERT INTO user (login, password, last_latitude, last_longitude, visible, token)
					VALUES (:login, :password, :last_latitude, :last_longitude, :visible, :token)
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
			$parameters = array(':id' => $data->getId(), ':login' => $data->getLogin(), ':password' => $data->getPassword(),
					':last_latitude' => $data->getLastLatitude(), ':last_longitude' => $data->getLastLongitude(),
					':visible' => $data->getVisible(), ':token' => $data->getToken());

			$stmt = $this->getConnection()->prepare('
				UPDATE user SET login = :login, password = :password, last_latitude = :last_latitude, last_longitude = :last_longitude,
				visible = :visible, token = :token WHERE id = :id
			');
			$stmt->execute($parameters);

			$id = $data->getId();
		}

		return $id;
	}

	public function delete($data) {
		$result = false;

		if($data !== null && $data instanceof User) {
			$parameters = array(':id' => $dat->getId());

			$stmt = $this->getConnection()->prepare('
				DELETE FROM user WHERE id = :id
			');
			$result = $stmt->execute($parameters);
		}

		return $result;
	}
}

?>