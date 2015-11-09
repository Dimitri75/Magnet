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
				SELECT id, login, latitude, longitude, last_activity FROM user WHERE id = :id
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
				SELECT u.id, u.login, u.latitude, u.longitude, u.last_activity FROM user u INNER JOIN group_has_users ghu ON ghu.id_user = u.id
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
			SELECT id, login, latitude, longitude, last_activity FROM user ORDER BY login
		');
		$stmt->execute();

		foreach($stmt->fetchAll() as $row) {
			$result[] = new GroupUser($row);
		}

		return $result;
	}

	public function save($data) {
		
	}

	public function update($data) {
		
	}

	public function delete($data) {
		
	}
}

?>