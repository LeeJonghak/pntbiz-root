$(window).load(function(){
	$("#help").on("click", function(e) {
	    var help = $("#help").attr("help");
	    try {
	    	$( "#modal-frame" ).remove();
	    } catch(e) {}
	    var iframe = "<iframe id='modal-frame' name='modal-frame' frameborder='0' width='100%' style='width:100%;'></iframe>";
	    $(".modal-body").append(iframe);
	    $("#modal").modal({
	        show: "true"
	    });
	    $('.modal').on('shown.bs.modal', function() {
	        $(this).find('iframe').attr('src',"/help/"+locale+"/"+help)
	    });
	    e.preventDefault();
	});
	parent.help.resizeHelp($(document).width(), $(document).height());
});

$(window).resize(function() {
	try {
		$("#modal-frame")[0].contentWindow.help.resize();
	} catch(e) {}
});

var help = {
	resizeHelp: function(width, height) {
		$(".modal-body").css({
			width: '100%',
			height: height + 50
		});
		$("#modal-frame").attr("width", width);
		$("#modal-frame").attr("height", height);
		
	},
	resize: function() {
		parent.help.resizeHelp($(document).width(), $(document).height());
	}
}