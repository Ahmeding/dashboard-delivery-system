import { Component, AfterViewInit, Renderer2, ElementRef, NgZone, HostListener } from '@angular/core';

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss']
})
export class SidebarComponent implements AfterViewInit {

  constructor(private renderer: Renderer2, private el: ElementRef, private ngZone: NgZone) {}

  ngAfterViewInit() {
    this.setupToggleButtons();
    //this.setupScrollEvent();
    this.setupMouseWheelEvent();
  }

  // Handle sidebar toggle on click
  private setupToggleButtons(): void {
    const sidebarToggle = this.el.nativeElement.querySelector('#sidebarToggle');
    const sidebarToggleTop = this.el.nativeElement.querySelector('#sidebarToggleTop');
    const body = document.body;
    const sidebar = this.el.nativeElement.querySelector('.sidebar');

    if (sidebarToggle) {
      this.renderer.listen(sidebarToggle, 'click', () => this.toggleSidebar(body, sidebar));
    }

    if (sidebarToggleTop) {
      console.log("errorrrrrrrrr---------------");
      this.renderer.listen(sidebarToggleTop, 'click', () => this.toggleSidebar(body, sidebar));
    }
  }
  private toggleSidebar(body: HTMLElement, sidebar: HTMLElement): void {
    if (body.classList.contains('sidebar-toggled')) {
      this.renderer.removeClass(body, 'sidebar-toggled');
    } else {
      this.renderer.addClass(body, 'sidebar-toggled');
    }

    if (sidebar.classList.contains('toggled')) {
      this.renderer.removeClass(sidebar, 'toggled');
    } else {
      this.renderer.addClass(sidebar, 'toggled');
    }

    if (sidebar.classList.contains('toggled')) {
      const collapses = sidebar.querySelectorAll('.collapse');
      collapses.forEach((collapse: Element) => {
        this.renderer.removeClass(collapse as HTMLElement, 'show'); // Collapsing logic for native JS
      });
    }
  }

/*
  private toggleSidebar(body: HTMLElement, sidebar: HTMLElement): void {
    this.renderer.toggleClass(body, 'sidebar-toggled');
    this.renderer.toggleClass(sidebar, 'toggled');

    if (sidebar.classList.contains('toggled')) {
      const collapses = sidebar.querySelectorAll('.collapse');
      collapses.forEach(collapse => {
        this.renderer.removeClass(collapse, 'show'); // Collapsing logic for native JS
      });
    }
  }*/

  // Handle window resize to toggle and collapse sidebar
  @HostListener('window:resize')
  onWindowResize() {
    const body = document.body;
    const sidebar = this.el.nativeElement.querySelector('.sidebar');

    if (window.innerWidth < 768) {
      const collapses = sidebar.querySelectorAll('.collapse');
      collapses.forEach((collapse: Element) => {
        this.renderer.removeClass(collapse, 'show'); // Collapse logic
      });
    }

    if (window.innerWidth < 480 && !sidebar.classList.contains('toggled')) {
      this.renderer.addClass(body, 'sidebar-toggled');
      this.renderer.addClass(sidebar, 'toggled');
      const collapses = sidebar.querySelectorAll('.collapse');
      collapses.forEach((collapse: Element) => {
        this.renderer.removeClass(collapse, 'show');
      });
    }
  }

  // Smooth scroll to top button
  private setupScrollEvent(): void {
    const scrollToTopButton = this.el.nativeElement.querySelector('.scroll-to-top');

    this.ngZone.runOutsideAngular(() => {
      window.addEventListener('scroll', () => {
        const scrollDistance = window.scrollY;
        if (scrollDistance > 100) {
          this.renderer.setStyle(scrollToTopButton, 'display', 'block');
        } else {
          this.renderer.setStyle(scrollToTopButton, 'display', 'none');
        }
      });
    });

    this.renderer.listen(scrollToTopButton, 'click', (event: Event) => {
      event.preventDefault();
      window.scrollTo({
        top: 0,
        behavior: 'smooth'
      });
    });
  }

  // Prevent scrolling when fixed sidebar is hovered
  private setupMouseWheelEvent(): void {
    const sidebar = this.el.nativeElement.querySelector('.sidebar');

    this.ngZone.runOutsideAngular(() => {
      this.renderer.listen(sidebar, 'wheel', (event: WheelEvent) => {
        if (window.innerWidth > 768) {
          const delta = event.deltaY;
          sidebar.scrollTop += (delta < 0 ? -1 : 1) * 30;
          event.preventDefault();
        }
      });
    });
  }
}
