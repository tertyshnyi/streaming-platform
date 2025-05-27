import {Component, OnInit, ViewChild} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormsModule} from '@angular/forms';
import {Router} from '@angular/router';
import {ToastComponent} from '../../components/admin-component/toast/toast.component';
import {MediaService} from '../../../core/services/media.service';

@Component({
  selector: 'app-admin-panel',
  standalone: true,
  imports: [CommonModule, FormsModule, ToastComponent],
  templateUrl: './admin-panel.component.html',
  styleUrl: './admin-panel.component.scss'
})
export class AdminPanelComponent implements OnInit {
  @ViewChild('toast') toast!: ToastComponent;

  searchQuery: string = '';
  confirmDeleteVisible = false;
  mediaToDelete: any = null;

  constructor(
    private router: Router,
    public mediaService: MediaService
  ) {}

  ngOnInit() {
    const showSeriesToast = history.state?.['showSeriesToast'];
    const showMovieToast = history.state?.['showMovieToast'];
    const showEditMovieToast = history.state?.['showEditMovieToast'];
    const showEditSeriesToast = history.state?.['showEditSeriesToast'];

    if (showSeriesToast) {
      setTimeout(() => this.toast.showSuccess('Series created successfully!'), 0);
      history.replaceState({}, '');
    }

    if (showMovieToast) {
      setTimeout(() => this.toast.showSuccess('Movie created successfully!'), 0);
      history.replaceState({}, '');
    }

    if (showEditMovieToast) {
      setTimeout(() => this.toast.showSuccess('Movie edited successfully!'), 0);
      history.replaceState({}, '');
    }

    if (showEditSeriesToast) {
      setTimeout(() => this.toast.showSuccess('Series edited successfully!'), 0);
      history.replaceState({}, '');
    }

    this.loadMedia();
  }

  loadMedia() {
    this.mediaService.getAllMedia().subscribe();
  }

  createMovie() {
    this.router.navigate(['admin/create-movie']);
  }

  createSeries() {
    this.router.navigate(['admin/create-series']);
  }

  editMedia(media: any) {
    const route = media.type === 'Series'
      ? ['/admin/edit-series', media.id]
      : ['/admin/edit-movie', media.id];

    this.router.navigate(route, { state: { media } });
  }

  showDeleteConfirmation(media: any) {
    this.mediaToDelete = media;
    this.confirmDeleteVisible = true;
  }

  confirmDelete(confirmed: boolean) {
    if (!confirmed) {
      this.toast.showWarning('Deletion cancelled');
      this.confirmDeleteVisible = false;
      this.mediaToDelete = null;
      return;
    }

    this.mediaService.deleteMedia(this.mediaToDelete.id).subscribe(() => {
      this.toast.showSuccess('Media deleted successfully!');
      this.confirmDeleteVisible = false;
      this.mediaToDelete = null;
    });
  }

  get filteredMediaList() {
    return this.mediaService.filteredMediaList(this.searchQuery);
  }
}
