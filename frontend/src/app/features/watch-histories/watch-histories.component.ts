import { Component, OnInit } from "@angular/core";
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-watch-histories',
  imports: [CommonModule],
  standalone: true,
  templateUrl: './watch-histories.component.html',
  styleUrls: ['./watch-histories.component.scss']
})
export class WatchHistoriesComponent implements OnInit {
  hasMediaItems: boolean = true;
  isModalOpen: boolean = false;

  sparkles = Array.from({ length: 50 }, () => ({
    top: Math.random() * window.innerHeight,
    left: Math.random() * window.innerWidth,
    delay: Math.random() * 2
  }));

  mediaItems = [
    { title: 'Future Man', date: '2025-04-28', time: '01:45', duration: '50:00', image: '/images/settings-background.jpg' },
    { title: 'Future Man', date: '2025-04-28', time: '08:45', duration: '50:00', image: '/images/settings-background.jpg' },
    { title: 'Future Man', date: '2025-04-28', time: '01:45', duration: '50:00', image: '/images/settings-background.jpg' },
    { title: 'Future Man', date: '2025-04-28', time: '08:45', duration: '50:00', image: '/images/settings-background.jpg' },
    { title: 'Future Man', date: '2025-04-28', time: '01:45', duration: '50:00', image: '/images/settings-background.jpg' },
    { title: 'Future Man', date: '2025-04-28', time: '08:45', duration: '50:00', image: '/images/settings-background.jpg' },
    { title: 'Future Man', date: '2025-04-28', time: '01:45', duration: '50:00', image: '/images/settings-background.jpg' },
    { title: 'Future Man', date: '2025-04-28', time: '08:45', duration: '50:00', image: '/images/settings-background.jpg' },
    { title: 'Future Man', date: '2025-04-28', time: '01:45', duration: '50:00', image: '/images/settings-background.jpg' },
    { title: 'Future Man', date: '2025-04-28', time: '08:45', duration: '50:00', image: '/images/settings-background.jpg' },
    { title: 'Future Man', date: '2025-04-28', time: '01:45', duration: '50:00', image: '/images/settings-background.jpg' },
    { title: 'Future Man', date: '2025-04-28', time: '08:45', duration: '50:00', image: '/images/settings-background.jpg' },
    { title: 'Future Man', date: '2025-04-28', time: '01:45', duration: '50:00', image: '/images/settings-background.jpg' },
    { title: 'Future Man', date: '2025-04-28', time: '08:45', duration: '50:00', image: '/images/settings-background.jpg' },
    { title: 'Future Man', date: '2025-04-28', time: '01:45', duration: '50:00', image: '/images/settings-background.jpg' },
    { title: 'Future Man', date: '2025-04-28', time: '08:45', duration: '50:00', image: '/images/settings-background.jpg' },
  ];

  visibleItems: { title: string; date: string; time: string; duration: string; image: string; }[] = [];
  pageSize = 12;
  currentPage = 0;

  ngOnInit(): void {
    this.loadMore();
  }

  loadMore() {
    const start = this.currentPage * this.pageSize;
    const end = start + this.pageSize;
    this.visibleItems = this.mediaItems.slice(0, end);
    this.currentPage++;
  }

  openConfirmationModal() {
    this.isModalOpen = true;
  }

  closeConfirmationModal() {
    this.isModalOpen = false;
  }

  clearList() {
    this.mediaItems = [];
    this.visibleItems = [];
    this.hasMediaItems = false;
    this.isModalOpen = false;
    console.log('The list is clear.');
  }

  getProgressPercentage(currentTime: string, duration: string): number {
    const toSeconds = (time: string): number => {
      const parts = time.split(':').map(Number);
      return parts.length === 3
        ? parts[0] * 3600 + parts[1] * 60 + parts[2]
        : parts[0] * 60 + parts[1];
    };

    const current = toSeconds(currentTime);
    const total = toSeconds(duration);
    if (total === 0) return 0;

    return Math.min((current / total) * 100, 100);
  }
}
