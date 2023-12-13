package moe.nmkmn.autoresourcepack.model;

public class Assets {
    public String url;
    public String id;
    public String node_id;
    public String name;
    public String label;
    public Uploader Uploader;
    public String content_type;
    public String state;
    public Number size;
    public Number download_count;
    public String created_at;
    public String updated_at;

    public Assets(String url, String id, String node_id, String name, String label, moe.nmkmn.autoresourcepack.model.Uploader uploader, String content_type, String state, Number size, Number download_count, String created_at, String updated_at, String browser_download_url) {
        this.url = url;
        this.id = id;
        this.node_id = node_id;
        this.name = name;
        this.label = label;
        Uploader = uploader;
        this.content_type = content_type;
        this.state = state;
        this.size = size;
        this.download_count = download_count;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.browser_download_url = browser_download_url;
    }

    public String  browser_download_url;
}
