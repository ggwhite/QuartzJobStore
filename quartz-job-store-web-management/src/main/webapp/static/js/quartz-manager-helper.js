/**
 * @author white
 */
(function( $ ) {
	
	var api_url = '/' + window.location.pathname.split('/')[1] + '/api/quartz';
	
	var _methods = {
		
	};
	
	var methods = {
		init : function(option) {
		
		},
		list : function (callback) {
			var url = api_url + '/list';
			$.get(url, callback);
		},
		pause : function (jobName, callback) {
			var url = api_url + '/pause/' + jobName;
			$.get(url, callback);
		},
		resume : function (jobName, callback) {
			var url = api_url + '/resume/' + jobName;
			$.get(url, callback);
		}
	};
	
	$.quartzManager = function( method ) {
    	if ( methods[method] ) {
      		return methods[method].apply( this, Array.prototype.slice.call( arguments, 1 ));
    	} else if ( typeof method === 'object' || ! method ) {
      		return methods.init.apply( this, arguments );
    	} else {
      		$.error( 'Method ' +  method + ' does not exist.' );
    	}
  	};
  	
})( jQuery );