$( document ).ready(function() {
    $('.log').click(function() {
        $('.login').hide();
        $('.newacc').show();
		$('.forgot').hide();
    });

    $('.alr').click(function() {
        $('.newacc').hide();
        $('.login').show();
		$('.forgot').hide();
    });

	$('.alrf').click(function() {
        $('.login').hide();
		$('.forgot').show();
		$('.newacc').hide();
    });

	

    
});