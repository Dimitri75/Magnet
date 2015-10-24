<?php

namespace Magnet\Model;

class GroupDAO extends DAO {
	public function __constrct(PDO $connection = null) {
		parent::__constrct($connection);
	}

	private function findCreator($creatorId) {
		$creator = null;

		if(isset($group) && $group->getId() !== null) {
			$userDAO = new UserDAO($this->getConnection());
			$creator = $userDAO->find($creatorId)
		}

		return $creator;
	}

	private function findUsers($groupId) {
		$users = array();

		if(isset($groupId)) {
			$parameters = array(':id_group' => $groupId);
			$stmt = $this->getConnection()->prepare('
				SELECT id_user FROM group_has_users WHERE id_group = :id_group
			');
			$stmt->execute($parameters);

			if($stmt->rowCount > 0) {
				$userDAO = new UserDAO($this->getConnection());
				foreach($stmt->findAll as $row)
					$users[] = $userDAO->find($row['id_user']);
				}
			}
		}

		return $users;
	}

	private function saveUsers($group) {
		$users = $group->getUsers()
		$userDAO = new UserDAO($this->getConnection());
		$stmt = $this->getConnection()->prepare('
			INSERT INTO group_has_users (id_group, id_user) VALUES (:id_group, :id_user)
		');
		$parameters = array('id_group' => $group->getId(), 'id_user' => 0);

		foreach($users as $user) {
			$idUser = $userDAO->save($user);
			$parameters['id_user'] = $idUser;
			$stmt->execute($parameters);
		}
	}

	public function find($id) {
		$result = null;

		if(is_numeric($id) && $id > 0) {
			$parameters = array(':id' => $id);

			$stmt = $this->getConnection()->prepare('
				SELECT * FROM group WHERE id = :id
			');
			$stmt->execute($parameters);

			if($stmt->rowCount > 0) {
				$row = $stmt->fetch();
				$result = new Group($row);
				$result->setCreator($this->findCreator($row['id_user']));
				$result->setUsers($this->findUsers($row['id']));
			}
		}

		return $result;
	}

	public function findAll() {
		$result = array();

		$stmt = $this->getConnection()->prepare('
			SELECT * FROM group ORDER BY name
		');
		$stmt->execute();

		foreach($stmt->fetchAll() as $row) {
			$group = new Group($row);
			$group->setCreator($this->findCreator($row['id_user']));
			$group->setUsers($this->findUsers($row['id']));
			$result[] = $group;
		}

		return $result;
	}

	public function save($data) {
		$id = null;

		if($data !== null && $data instanceof Group) {
			if($data->getId() !== null) {
				$id = $this->update($data);
			}
			else {
				$userDAO = new UserDAO($this->getConnection());
				$idCreator = $userDAO->save($data->getCreator());
				$this->saveUsers($data);
				$parameters = array('name' => $data->getName(), 'id_user' => $idCreator);

				$stmt = $this->getConnection()->prepare('
					INSERT INTO user name, id_user) VALUES (:name, :id_user)
				');
				$stmt->execute($parameters);

				$id = $this->getConnection()->lastInsertId();
			}
		}

		return $id;
	}

	public function update($data) {
		$id = null;

		if($data !== null && $data instanceof Group) {
			$userDAO = new UserDAO($this->getConnection());
			$idCreator = $userDAO->save($data->getCreator());
			$this->saveUsers($data);
			$parameters = array('name' => $data->getName(), 'id_user' => $idCreator);

			$parameters = array('id' => $data->getId(), 'name' => $data-getLogin(), 'id_user' => $data->getPassword());
			$stmt = $this->getConnection()->prepare('
				UPDATE user SET name = :name, id_user = :id_user WHERE id = :id
			');
			$stmt->execute($parameters);

			$id = $data['id'];
		}

		return $id;
	}

	public function delete($data) {
		$result = false;

		if($data !== null && $data instanceof Group) {
			$stmt = $this->getConnection()->prepare('
				DELETE FROM group_has_users WHERE id_group = :id_group
			');
			$parameters = array('id_group' => $data->getId());
			$stmt->execute($parameters);

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