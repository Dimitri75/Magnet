<?php

namespace Magnet\Model;

class GroupDAO extends DAO {
	public function __constrct(PDO $connection = null) {
		parent::__constrct($connection);
	}

	private function saveUsers($group) {
		$users = $group->getUsers();
		$userDAO = new GroupUserDAO($this->getConnection());

		$stmt1 = $this->getConnection()->prepare('
			SELECT * FROM group_has_users WHERE id_group = :id_group AND id_user = :id_user
		');

		$stmt2 = $this->getConnection()->prepare('
			INSERT INTO group_has_users (id_group, id_user) VALUES (:id_group, :id_user)
		');
		$parameters = array('id_group' => $group->getId(), 'id_user' => 0);

		foreach($users as $user) {
			if($user->getId() === null) {
				$user = $userDAO->findByLogin($user->getLogin());
			}

			$parameters['id_user'] = $user->getId();

			$stmt1->execute($parameters);
			if($stmt1->rowCount() === 0) {
				$stmt2->execute($parameters);
			}			
		}
	}

	public function find($id) {
		$result = null;

		if(is_numeric($id) && $id > 0) {
			$parameters = array(':id' => $id);

			$stmt = $this->getConnection()->prepare('
				SELECT * FROM groups WHERE id = :id
			');
			$stmt->execute($parameters);

			if($stmt->rowCount() > 0) {
				$row = $stmt->fetch();
				$result = new Group($row);
				
				$groupUserDAO = new GroupUserDAO($this->getConnection());
				$result->setCreator($groupUserDAO->find($row['id_user']));
				$result->setUsers($groupUserDAO->findbyGroupId($row['id']));
			}
		}

		return $result;
	}

	public function findByUserId($userId) {
		$result = array();

		$parameters = array(':id_user' => $userId);
		$stmt = $this->getConnection()->prepare('
			SELECT * FROM groups g INNER JOIN group_has_users ghu ON ghu.id_group = g.id WHERE ghu.id_user = :id_user ORDER BY name
		');
		$stmt->execute($parameters);

		foreach($stmt->fetchAll() as $row) {
			$group = new Group($row);
			$groupUserDAO = new GroupUserDAO($this->getConnection());
			$group->setCreator($groupUserDAO->find($row['id_user']));
			$group->setUsers($groupUserDAO->findbyGroupId($row['id']));
			$result[] = $group;
		}

		return $result;
	}

	public function findAll() {
		$result = array();

		$stmt = $this->getConnection()->prepare('
			SELECT * FROM groups ORDER BY name
		');
		$stmt->execute();

		foreach($stmt->fetchAll() as $row) {
			$group = new Group($row);
			$groupUserDAO = new GroupUserDAO($this->getConnection());
			$group->setCreator($groupUserDAO->find($row['id_user']));
			$group->setUsers($groupUserDAO->findbyGroupId($row['id']));
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
				$parameters = array(':name' => $data->getName(), ':id_user' => $data->getCreator()->getId());

				$stmt = $this->getConnection()->prepare('
					INSERT INTO groups (name, id_user) VALUES (:name, :id_user)
				');
				$stmt->execute($parameters);

				$id = $this->getConnection()->lastInsertId();
				$data->setId($id);
				$this->saveUsers($data);
			}
		}

		return $id;
	}

	public function update($data) {
		$id = null;

		if($data !== null && $data instanceof Group) {
			$this->saveUsers($data);

			$parameters = array(':id' => $data->getId(), ':name' => $data->getName(), ':id_user' => $data->getCreator()->getId());
			$stmt = $this->getConnection()->prepare('
				UPDATE groups SET name = :name, id_user = :id_user WHERE id = :id
			');
			$stmt->execute($parameters);

			$id = $data->getId();
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
				DELETE FROM groups WHERE id = :id
			');
			$result = $stmt->execute($parameters);
		}

		return $result;
	}
}

?>