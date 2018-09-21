function main() {
	fl.outputPanel.clear();

	var ls = fl.getDocumentDOM().getTimeline().layers;

	for (var i = 0; i < ls.length; ++i) {
		handleLayer(ls[i]);
	}
}

function handleLayer(l) {
	var fs = l.frames;

	for (var i = 0; i < fs.length; ++i) {
		handleFrame(fs[i]);
	}
}

function handleFrame(f) {
	var els = f.elements;

	for (var i = 0; i < els.length; ++i) {
		el = els[i];

		debug_arr.length = 0;
		debug_arr.push("Element=" + el);
		debug_arr.push("elementType=\"" + el.elementType + "\"");

		//el.x = Math.ceil(Math.floor(el.x));
		//el.y = Math.ceil(Math.floor(el.y));

		switch (el.elementType) {
			case "text":
				handleText(el);
				break;
			case "instance":
				handleInstance(el);
				break;
			default:
				debug_arr.push("Error=Invalid elementType \"" + el.elementType + "\"");
		}

		traceDebug();
	}
}

function handleText(e) {
	if ("static" == e.textType) {
		debug_arr.push("Error=Invalid textType \"" + e.textType + "\"");
	}

	outputCode(e.name, "TextField");
}

function handleInstance(e) {
	debug_arr.push("instanceType=\"" + e.instanceType + "\"");
	debug_arr.push("libraryItem=\"" + e.libraryItem);

	switch (e.instanceType) {
		case "symbol":
			{
				debug_arr.push("symbolType=\"" + e.symbolType + "\"");
				debug_arr.push("libraryItem.name=\"" + e.libraryItem.name + "\"");

				switch (e.symbolType) {
					case "movie clip":
						{
							var c_type = "Sprite";
							if (1 < e.libraryItem.timeline.frameCount) {
								c_type = "MovieClip";
							}
							outputCode(e.name, c_type);
							break;
						}
					case "button":
						{
							outputCode(e.name, "SimpleButton");
							break;
						}
					case "graphic":
						{
							break;
						}
					default:
						{
							debug_arr.push("Error=Invalid symbolType \"" + e.symbolType + "\"");
						}
				}

				break;
			}
		case "bitmap":
			{
				debug_arr.push("libraryItem.name=\"" + e.libraryItem.name + "\"");

				break;
			}
		default:
			{
				debug_arr.push("Error=Invalid instanceType \"" + el.instanceType + "\"");
			}
	}
}

function outputCode(e_name, e_type) {
	if ("" == e_name) {
		debug_arr.push("Error=No name");
		return;
	}

	debug_arr.push("Pass");

	if (!debug) fl.trace("private function get " + e_name + "():" + e_type + "{ return uiSkin[\"" + e_name + "\"]; }");
}

function traceDebug() {
	if (debug) fl.trace(debug_arr.join(", "));
}

var debug = false;
var debug_arr = [];
main();