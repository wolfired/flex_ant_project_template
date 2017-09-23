function disperse(cf, i) {
	var tl = document.getTimeline();

	if (0 < cf) {
		tl.insertBlankKeyframe(cf);
	}

	tl.currentFrame = cf;
	document.addItem({
		x: 0,
		y: 0
	}, i);
}

function main() {
	fl.outputPanel.clear();

	document.selectAll();

	var arr = document.selection;

	arr.sort(function (fst, snd) {
		if (mode) {
			if (fst.x > snd.x) {
				return 1;
			}
			return -1;
		}

		if (fst.y > snd.y) {
			return 1;
		}
		return -1;
	});

	document.deleteSelection();

	for (var i = 0; i < arr.length; ++i) {
		disperse(i, arr[i].libraryItem);
	}

	align();
}

function align() {
	var arr;
	var max_w = 0;
	var max_h = 0;

	foreach_frame(function () {
		document.selectAll();
		arr = document.selection;

		arr[0].x = 0;
		arr[0].y = 0;

		if (max_w < arr[0].width) {
			max_w = arr[0].width;
		}

		if (max_h < arr[0].height) {
			max_h = arr[0].height;
		}
	});

	if (!need_align) {
		return;
	}

	foreach_frame(function () {
		document.selectAll();
		arr = document.selection;

		arr[0].x = Math.floor((max_w - arr[0].width) / 2);
		arr[0].y = Math.floor((max_h - arr[0].height) / 2);
	});
}

function foreach_frame(fn) {
	var tl = document.getTimeline();

	for (var i = 0; i < tl.frameCount; ++i) {
		tl.currentFrame = i;
		fn();
	}
}

var need_align = confirm("true: 居中对齐, false: 左上角对齐");
var mode = confirm("true: 按X坐标从小到大排序, false: 按Y坐标从小到大排序");
main();