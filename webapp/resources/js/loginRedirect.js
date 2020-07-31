window.onload = function() {
	function countdown() {
		if (typeof countdown.counter == 'undefined') {
			countdown.counter = 3;
		}
		if (countdown.counter > 0) {
			document.getElementById('count').innerHTML = countdown.counter--;
			setTimeout(countdown, 1000);
		} else {
			location.href = 'login.jsf?faces-redirect=true';
		}
	}
	countdown();
};
