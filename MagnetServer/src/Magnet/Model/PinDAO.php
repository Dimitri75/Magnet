<?php

namespace Magnet\Model;

class PinDAO extends DAO {
	public function __constrct(PDO $connection = null) {
		parent::__constrct($connection);
	}

	public function find($id) {
		$result = null;

		if(is_numeric($id) && $id > 0) {
			$parameters = array(':id' => $id);

			$stmt = $this->getConnection()->prepare('
				SELECT * FROM pin WHERE id = :id
			');
			$stmt->execute($parameters);

			if($stmt->rowCount() > 0) {
				$row = $stmt->fetch();
				$result = new Pin($row);
				$groupUserDAO = new GroupUserDAO($this->getConnection());
				$result->setCreator($groupUserDAO->find($row['id_user']));
			}
		}

		return $result;
	}

	public function findByUserId($userId) {
		$result = array();

		$parameters = array(':id_user' => $userId);
		$stmt = $this->getConnection()->prepare('
			SELECT * FROM pin p INNER JOIN groups g ON g.id = p.id_group INNER JOIN group_has_users ghu ON ghu.id_group = g.id
			WHERE ghu.id_user = :id_user ORDER BY p.name
		');
		$stmt->execute($parameters);

		$groupUserDAO = new GroupUserDAO($this->getConnection());
		foreach($stmt->fetchAll() as $row) {
			$pin = new Pin($row);
			$pin->setCreator($groupUserDAO->find($row['id_user']));
			$result[] = $pin;
		}

		return $result;
	}

	public function findByGroupId($groupId) {
		$result = array();

		$parameters = array(':id_group' => $groupId);
		$stmt = $this->getConnection()->prepare('
			SELECT * FROM pin
			WHERE id_group = :id_group ORDER BY name
		');
		$stmt->execute($parameters);

		$groupUserDAO = new GroupUserDAO($this->getConnection());
		foreach($stmt->fetchAll() as $row) {
			$pin = new Pin($row);
			$pin->setCreator($groupUserDAO->find($row['id_user']));
			$result[] = $pin;
		}

		return $result;
	}

	public function findAll() {
		$result = array();

		$stmt = $this->getConnection()->prepare('
			SELECT * FROM pin ORDER BY name
		');
		$stmt->execute();

		$groupUserDAO = new GroupUserDAO($this->getConnection());
		foreach($stmt->fetchAll() as $row) {
			$pin = new Pin($row);
			$pin->setCreator($groupUserDAO->find($row['id_user']));
			$result[] = $pin;
		}

		return $result;
	}

	public function save($data) {
		$id = null;

		if($data !== null && $data instanceof Pin) {
			if($data->getId() !== null) {
				$id = $this->update($data);
			}
			else {
				$parameters = array(':name' => $data->getName(), ':description' => $data->getDescription(), ':latitude' => $data->getLocation()->getLatitude(),
					':longitude' => $data->getLocation()->getLongitude(), ':creation_time' => $data->getCreationTime(), ':deletion_time' => $data->getDeletionTime(),
					':id_user' => $data->getCreator()->getId(), ':id_group' => $data->getIdGroup());

				$stmt = $this->getConnection()->prepare('
					INSERT INTO pin (name, description, latitude, longitude, creation_time, deletion_time, id_user, id_group)
					VALUES (:name, :description, :latitude, :longitude, :creation_time, :deletion_time, :id_user, :id_group)
				');
				$stmt->execute($parameters);

				$id = $this->getConnection()->lastInsertId();
			}
		}

		return $id;
	}

	public function update($data) {
		$id = null;

		if($data !== null && $data instanceof Pin) {
			$this->saveUsers($data);

			$parameters = array(':name' => $data->getName(), ':description' => $data->getDescription(), ':latitude' => $data->getLocation()->getLatitude(),
					':longitude' => $data->getLocation()->getLongitude(), ':creation_time' => $data->getCreationTime(), ':deletion_time' => $data->getDeletionTime(),
					':id_user' => $idCreator, ':id_group' => $data->getIdGroup(), ':id' => $data->getId());
			$stmt = $this->getConnection()->prepare('
				UPDATE pin SET name = :name, description = :description, latitude = :latitude, :longitude = :longitude, creation_time = :creation_time,
				deletion_time = :deletion_time, id_user = :id_user, id_group = :id_group WHERE id = :id
			');
			$stmt->execute($parameters);

			$id = $data->getId();
		}

		return $id;
	}

	public function delete($data) {
		$result = false;

		if($data !== null && $data instanceof Pin) {
			$parameters = array('id' => $dat->getId());

			$stmt = $this->getConnection()->prepare('
				DELETE FROM pin WHERE id = :id
			');
			$result = $stmt->execute($parameters);
		}

		return $result;
	}
}

?>