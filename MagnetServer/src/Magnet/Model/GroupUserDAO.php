<?php

namespace Magnet\Model;

class GroupUserDAO extends DAO {
	public function __constrct(PDO $connection = null) {
		parent::__constrct($connection);
	}

	public function find($id) {
		$result = null;

		if(is_numeric($id) && $id > 0) {
			$parameters = array(':id' => $id);

			$stmt = $this->getConnection()->prepare('
				SELECT id, login, latitude, longitude, last_activity, visible FROM user WHERE id = :id
			');
			$stmt->execute($parameters);

			if($stmt->rowCount() > 0) {
				$result = new GroupUser($stmt->fetch());
			}
		}

		return $result;
	}

	public function findByLogin($login) {
		$result = null;

		if(is_string($login)) {
			$parameters = array(':login' => $login);

			$stmt = $this->getConnection()->prepare('
				SELECT id, login, latitude, longitude, last_activity, visible FROM user WHERE login = :login
			');
			$stmt->execute($parameters);

			if($stmt->rowCount() > 0) {
				$result = new GroupUser($stmt->fetch());
			}
		}

		return $result;
	}

	public function findByGroupId($groupId) {
		$result = array();

		if(is_numeric($groupId) && $groupId > 0) {
			$parameters = array(':id_group' => $groupId);
			$stmt = $this->getConnection()->prepare('
				SELECT u.id, u.login, u.latitude, u.longitude, u.last_activity, u.visible FROM user u INNER JOIN group_has_users ghu ON ghu.id_user = u.id
				WHERE ghu.id_group = :id_group
			');
			$stmt->execute($parameters);

			foreach($stmt->fetchAll() as $row) {
				$result[] = new GroupUser($row);
			}
		}

		return $result;
	}

	public function findAll() {
		$result = array();

		$stmt = $this->getConnection()->prepare('
			SELECT id, login, latitude, longitude, last_activity, visible FROM user ORDER BY login
		');
		$stmt->execute();

		foreach($stmt->fetchAll() as $row) {
			$result[] = new GroupUser($row);
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
				$parameters = array(':login' => $data->getLogin(), ':latitude' => $data->getLocation()->getLatitude(),
					':longitude' => $data->getLocation()->getLongitude(), ':last_activity' => $data->getLastActivity());

				$stmt = $this->getConnection()->prepare('
					INSERT INTO user (login, latitude, longitude, last_activity)
					VALUES (:login, :latitude, :longitude, :last_activity)
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
			$parameters = array(':id' => $data->getId(), ':login' => $data->getLogin(), ':latitude' => $data->getLocation()->getLatitude(),
				':longitude' => $data->getLocation()->getLongitude(), ':last_activity' => $data->getLastActivity());

			$stmt = $this->getConnection()->prepare('
				UPDATE user SET login = :login, latitude = :latitude, longitude = :longitude, last_activity = :last_activity WHERE id = :id
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