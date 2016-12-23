var NmcJobUtil = {};


NmcJobUtil.addOverlay = function (selector) {
	if (selector) {
		selector.css('position', 'relative');
		selector.append('<div id="overlay_inside"><div class="mask"></div></div>')
	} else {
		$('body').append('<div id="overlay"><div class="mask"></div></div>');
		$('#overlay').show();
	}
};

NmcJobUtil.removeOverlay = function (selector) {
	if (selector) {
		$('#overlay_inside').remove();
		selector.find('#overlay_inside').remove();
	} else {
		$('#overlay').remove();
	}
}