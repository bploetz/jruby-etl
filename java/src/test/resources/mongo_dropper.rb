class MongoDBDropper
  def drop(str)
    @mongo_config = YAML.load(str)
    database_name = @mongo_config["connector"]["database"]
    db = Mongo::Connection.new.db(database_name)
    connection = db.connection
    connection.drop_database(database_name)
  end
end
