document.addEventListener('DOMContentLoaded', function() {
    const hamburger = document.getElementById('hamburger');
    const navMenu = document.getElementById('navMenu');
    const body = document.body; // Select body
    const overlay = document.getElementById('menuOverlay'); // Select overlay

    // Function to close menu
    function closeMenu() {
        hamburger.classList.remove('active');
        navMenu.classList.remove('active');
        body.classList.remove('menu-open'); // Restore scrolling
        if(overlay) overlay.classList.remove('active'); // Hide overlay
    }

    // Function to open/toggle menu
    function toggleMenu() {
        hamburger.classList.toggle('active');
        navMenu.classList.toggle('active');
        body.classList.toggle('menu-open'); // Stop/Start scrolling
        if(overlay) overlay.classList.toggle('active'); // Toggle overlay
    }

    if (hamburger && navMenu) {
        hamburger.addEventListener('click', toggleMenu);

        // Close menu when clicking on a link
        const menuLinks = document.querySelectorAll('.menu a');
        menuLinks.forEach(link => {
            link.addEventListener('click', closeMenu);
        });

        // Close menu when clicking the overlay
        if (overlay) {
            overlay.addEventListener('click', closeMenu);
        }

        // Close menu when clicking outside (fallback)
        document.addEventListener('click', function(event) {
            const isClickInsideNav = hamburger.contains(event.target) || navMenu.contains(event.target);
            // Only close if active and click is NOT on overlay (overlay has its own listener)
            if (!isClickInsideNav && navMenu.classList.contains('active') && event.target !== overlay) {
                closeMenu();
            }
        });
    }
});