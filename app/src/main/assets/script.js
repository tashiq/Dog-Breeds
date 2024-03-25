var t=gsap.timeline();



gsap.from("span",{
    onStart: function() {
        $('span').textillate({
             in: {
                 effect: 'fadeInUpBig' ,

                },

            });
    }
})