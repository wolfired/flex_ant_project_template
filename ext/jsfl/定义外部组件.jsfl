function main() {
	fl.outputPanel.clear();

	var doc = fl.getDocumentDOM();
	var lib = doc.library;

	var root_name = doc.name.replace(fla_name_regex, "$1");
	pkg_path = root_name + ".skin.";

	var reg = new RegExp("^" + root_name + "/" + output_dir, "g");

	lib.selectAll(true);
	var arr = lib.getSelectedItems();
	for (var i = 0; i < arr.length; ++i) {
		var name_str = arr[i].name;
		if (-1 === name_str.search(reg)) {
			continue;
		}
		//fl.trace(arr[i] + "\t" + arr[i].name + "\t" + arr[i].itemType + "\t" + arr[i].timeline);

		var handle = handle_map[arr[i].itemType];
		if (undefined !== handle) {
			handle(arr[i]);
		}
	}
	lib.selectAll(false);

	doc.save();
}

function handleMovieClip(item) {
	var clz_name = item.name.replace(target_name_regex, "$1");

	item.linkageExportForAS = true;
	item.linkageExportInFirstFrame = true;
	item.linkageClassName = pkg_path + clz_name;

	if (1 < item.timeline.frameCount) {
		item.linkageBaseClass = "flash.display.MovieClip";
	} else {
		item.linkageBaseClass = "flash.display.Sprite";
	}
}

function handleButton(item) {
	var clz_name = item.name.replace(target_name_regex, "$1");

	item.linkageExportForAS = true;
	item.linkageExportInFirstFrame = true;
	item.linkageClassName = pkg_path + clz_name;
}

function handleBitmap(item) {
	//fl.trace(item + "\t" + item.name + "\t" + item.itemType + "\t" + item.timeline);
	var clz_name = item.name.replace(target_name_regex, "$1");

	item.linkageExportForAS = true;
	item.linkageExportInFirstFrame = true;
	item.linkageClassName = pkg_path + clz_name;
}

var fla_name_regex = /ui(\w+)\.fla/g;
var pkg_path;
var target_name_regex = /.*\/([a-zA-Z0-9]+)(\.png)?/g;
var output_dir = "output";
var handle_map = {
	"folder": function () {},
	"movie clip": handleMovieClip,
	"button": handleButton,
	"bitmap": handleBitmap,
};
main();