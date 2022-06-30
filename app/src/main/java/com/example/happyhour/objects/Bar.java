package com.example.happyhour.objects;

import java.util.ArrayList;
import java.util.HashMap;

public class Bar {
    private String name = "";
    private String description = "";
    private String id = "";
    private String happy_hour = "";
    private String owner_id = "";
    private String bar_photo = "";
    private String menu_photo = "";
    private eBarType barType;
    private ArrayList<eMusicType> musicTypes = new ArrayList<>();
    private HashMap<String , Review> reviews =new HashMap<String , Review>();
    private HashMap<String , Follower> followers = new HashMap<>();
    private HashMap<String,Table> tables = new HashMap<>();
    private HashMap<String,Post> posts = new HashMap<>();
    private MyTime openTime = new MyTime();
    private MyTime closingTime = new MyTime();
    private AddressMaps addressMaps = new AddressMaps();



    public Bar() {
    }

    public String getName() {
        return name;
    }

    public Bar setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Bar setDescription(String description) {
        this.description = description;
        return this;
    }
    public float starsAvg(){
        int size = this.reviews.size();
        float sum = 0;
        for (Review review: this.reviews.values()) {
            sum+=review.getStars_rating();
        }
        if(size != 0 )
            return sum/(float) size;
        return 0;
    }

    public HashMap<String , Review> getReviews() {
        return reviews;
    }

    public Bar setReviews(HashMap<String , Review> reviews) {
        this.reviews = reviews;
        return this;
    }
    public Bar add_review(String id, Review review) {
        this.reviews.put(id,review);
        return this;
    }

    public eBarType getBarType() {
        return barType;
    }

    public Bar setBarType(eBarType barType) {
        this.barType = barType;
        return this;
    }

    public ArrayList<eMusicType> getMusicTypes() {
        return musicTypes;
    }

    public Bar setMusicTypes(ArrayList<eMusicType> musicTypes) {
        this.musicTypes = musicTypes;
        return this;
    }

    public MyTime getOpenTime() {
        return openTime;
    }

    public Bar setOpenTime(MyTime openTime) {
        this.openTime = openTime;
        return this;
    }

    public MyTime getClosingTime() {
        return closingTime;
    }

    public Bar setClosingTime(MyTime closingTime) {
        this.closingTime = closingTime;
        return this;
    }

    public HashMap<String , Follower> getFollowers() {
        return followers;
    }

    public Bar setFollowers(HashMap<String , Follower> followers) {
        this.followers = followers;
        return this;
    }
    public Bar setFollower(String userID, Follower follower) {
        this.followers.put(userID , follower);
        return this;
    }

    public HashMap<String,Table> getTables() {
        return tables;
    }

    public Bar setTables(HashMap<String,Table> tables) {
        this.tables = tables;
        return this;
    }
    public Bar addTable(String id,Table table) {
        this.tables.put(id , table);
        return this;
    }
    public Bar removeTable(String id) {
        this.tables.remove(id);
        return this;
    }

    public String getId() {
        return id;
    }

    public Bar setId(String id) {
        this.id = id;
        return this;
    }

    public String getOwner_id() {
        return owner_id;
    }

    public Bar setOwner_id(String owner_id) {
        this.owner_id = owner_id;
        return this;
    }

    public String getHappy_hour() {
        return happy_hour;
    }

    public Bar setHappy_hour(String happy_hour) {
        this.happy_hour = happy_hour;
        return this;
    }

    public String barTypeToString() {
        if(barType == null){
            return " ";
        }
        return barType.toString().replace('_',' ');

    }

    public String barMusicToString() {
        if(musicTypes.size() == 0){
            return " ";
        }
        StringBuffer buffer = new StringBuffer();
        musicTypes.forEach(type -> {
            buffer.append(type.toString().replace('_',' '));
            if(musicTypes.indexOf(type) != (musicTypes.size()-1)) // if not last
                buffer.append(", ");
        });
        return buffer.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bar bar = (Bar) o;
        return bar.owner_id.equals(owner_id) && bar.id.equals(id);
    }

    public String getBar_photo() {
        return bar_photo;
    }

    public Bar setBar_photo(String bar_photo) {
        this.bar_photo = bar_photo;
        return this;
    }

    public String getMenu_photo() {
        return menu_photo;
    }

    public Bar setMenu_photo(String menu_photo) {
        this.menu_photo = menu_photo;
        return this;
    }

    public HashMap<String, Post> getPosts() {
        return posts;
    }

    public Bar setPosts(HashMap<String, Post> posts) {
        this.posts = posts;
        return this;
    }

    public void addPost(Post post) {
        this.posts.put(post.getPost_id() , post);
    }
    public void removePost(String post_id) {
        this.posts.remove(post_id);
    }

    public AddressMaps getAddressMaps() {
        return addressMaps;
    }

    public Bar setAddressMaps(AddressMaps addressMaps) {
        this.addressMaps = addressMaps;
        return this;
    }
}
