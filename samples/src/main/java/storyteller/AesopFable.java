package storyteller;

public class AesopFable {

    private final String name;
    private final String story_content;
    private final String moral;

    AesopFable (String name, String story_content, String moral) {
        this.name = name;
        this.story_content = story_content;
        this.moral = moral;
    }
    
    public String getStoryName() {
    	return this.name;
    }
    
    public String getStoryContent() {
    	return this.story_content;
    }
    
    public String getStoryMoral() {
    	return this.moral;
    }
}


