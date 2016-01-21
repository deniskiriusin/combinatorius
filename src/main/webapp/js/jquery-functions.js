$.fn.timedDisable = function(time) {
    if (time == null) { time = 5000; }
    return $(this).each(function() {
        $(this).attr('disabled', 'disabled');
        var disabledElem = $(this);
        setTimeout(function() {
            disabledElem.removeAttr('disabled');
        }, time);
    });
};

$.fn.blink = function() {
	return $(this).each(function() {
		var bgColor = $('#directory_tree').css('background-color');
		$(this).addClass("highlighted");
		$(this).animate({ backgroundColor: bgColor }, 500 )
		.animate({ backgroundColor: "transparent" }, 500)
		.animate({ backgroundColor: bgColor }, 500 )
		.animate({ backgroundColor: "transparent" }, 500)
	});
};
