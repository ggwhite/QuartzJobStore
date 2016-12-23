var table = $('#jobList');

$(document).ready(function() {
	bindBtnControlEvent($('#refresh-btn'));
	refresh();
});

function refresh() {
	NmcJobUtil.addOverlay(table);
	$.quartzManager('list', function(list) {
		NmcJobUtil.removeOverlay(table);
		cleanTbody(table);
		for (var i = 0; i < list.length; i++) {
			addJob(table, list[i]);
		}
	});
}

function bindBtnControlEvent(btn) {
	btn.on('click', function() {
		var action = $(this).attr('action');
		var jobName = $(this).attr('jobName');
		if (action == 'pause') {
			$.quartzManager('pause', jobName, function(res) {
				refresh()
			});
		} else if (action == 'resume') {
			$.quartzManager('resume', jobName, function(res) {
				refresh()
			});
		} else if (action == 'refresh') {
			refresh();
		}

		return false;
	});
}

function cleanTbody(table) {
	table.find('tbody tr').remove();
}

function addJob(table, job) {
	var tbody = table.find('tbody');
	var tr = $('<tr></tr>');
	var td_name = $('<td>' + (job.name ? job.name : '') + '</td>');
	var td_description = $('<td>' + (job.description ? job.description : '')
			+ '</td>');
	var td_state = $('<td>' + (job.state ? job.state : '') + '</td>');
	var td_lastExecuteTime = $('<td>'
			+ (job.lastExecuteTime ? job.lastExecuteTime : '') + '</td>');
	var td_nextExecuteTime = $('<td>'
			+ (job.nextExecuteTime ? job.nextExecuteTime : '') + '</td>');
	var td_control = $('<td></td>');

	var btn_control = $('<button>');
	btn_control.addClass('btn');
	btn_control.addClass('btn-control');
	btn_control.attr('jobName', (job.name ? job.name : ''));

	if (job.state == 'NORMAL') {
		btn_control.text('Pause');
		btn_control.addClass('btn-danger');
		btn_control.attr('action', 'pause');
	} else if (job.state == 'PAUSED') {
		btn_control.text('Start');
		btn_control.addClass('btn-primary');
		btn_control.attr('action', 'resume');
	}

	td_control.append(btn_control);

	tr.append(td_name);
	tr.append(td_description);
	tr.append(td_state);
	tr.append(td_lastExecuteTime);
	tr.append(td_nextExecuteTime);
	tr.append(td_control);

	tbody.append(tr);

	// bind event
	bindBtnControlEvent(btn_control);
}