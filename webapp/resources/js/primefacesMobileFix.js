var originalPrimeFacesCw = PrimeFaces.cw;
PrimeFaces.cw = function(name, id, options, resource) {
	resource = resource || name.toLowerCase();
	originalPrimeFacesCw.apply(this, [ name, id, options, resource ]);
};
