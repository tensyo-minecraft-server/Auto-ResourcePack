package moe.nmkmn.texture_updater.model;

import java.util.List;

public class Release {
    public String url;
    public String assets_url;
    public String upload_url;
    public String html_url;
    public Number id;
    public String node_id;
    public String tag_name;
    public String target_commitish;
    public String name;
    public Boolean draft;
    public Boolean prerelease;
    public String created_at;
    public String published_at;
    public List<Assets> assets;
    public String tarball_url;
    public String zipball_url;
    public String body;

    public Release(String url, String assets_url, String upload_url, String html_url, Number id, String node_id, String tag_name, String target_commitish, String name, Boolean draft, Boolean prerelease, String created_at, String published_at, List<Assets> assets, String tarball_url, String zipball_url, String body) {
        this.url = url;
        this.assets_url = assets_url;
        this.upload_url = upload_url;
        this.html_url = html_url;
        this.id = id;
        this.node_id = node_id;
        this.tag_name = tag_name;
        this.target_commitish = target_commitish;
        this.name = name;
        this.draft = draft;
        this.prerelease = prerelease;
        this.created_at = created_at;
        this.published_at = published_at;
        this.assets = assets;
        this.tarball_url = tarball_url;
        this.zipball_url = zipball_url;
        this.body = body;
    }
}
