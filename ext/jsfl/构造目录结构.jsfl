function main() {
	fl.outputPanel.clear();

	var doc = fl.getDocumentDOM();
	var lib = doc.library;

	var root_name = doc.name.replace(fla_name_regex, "$1");

	//if (!lib.itemExists(root_name)) {
	for (var i = 0; i < sub_names.length; ++i) {
		lib.addNewItem("folder", root_name + "/" + sub_names[i]);
	}
	//}

	doc.save();
}

var fla_name_regex = /ui(\w+)\.fla/g;
var sub_names = ["btn", "img", "mc", "output", "custom"];
main();