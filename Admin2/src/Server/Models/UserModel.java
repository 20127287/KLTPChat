package Server.Models;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

public class UserModel {
	public MongoCollection<Document> CollectionUser() {
		String uri = "mongodb+srv://admin:admin123@cluster0.wbqiils.mongodb.net/?retryWrites=true&w=majority";
		MongoClientURI mongoClientURI = new MongoClientURI(uri);
		MongoClient mongoClient = new MongoClient(mongoClientURI);
		MongoDatabase mongoDatabase = mongoClient.getDatabase("Database");

		MongoCollection<Document> collection = mongoDatabase.getCollection("User");
		System.out.println("Connection successful!");
		return collection;
	}
}