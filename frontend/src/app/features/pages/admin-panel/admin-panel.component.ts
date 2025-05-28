import { Component, OnInit, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { ToastComponent } from '../../components/admin-component/toast/toast.component';
import { MediaService } from '../../../core/services/media.service';
import { MediaCardModel } from '../../../core/models/media-card.model';
import { SelectionService } from '../../../core/services/selection.service';
import { Subject, of } from 'rxjs';
import { debounceTime, distinctUntilChanged, switchMap } from 'rxjs/operators';

@Component({
  selector: 'app-admin-panel',
  standalone: true,
  imports: [CommonModule, FormsModule, ToastComponent],
  templateUrl: './admin-panel.component.html',
  styleUrls: ['./admin-panel.component.scss']
})
export class AdminPanelComponent implements OnInit {
  @ViewChild('toast') toast!: ToastComponent;

  searchQuery: string = '';
  confirmDeleteVisible = false;
  mediaToDelete: MediaCardModel | null = null;

  filteredMediaList: MediaCardModel[] = [];

  private searchSubject = new Subject<string>();

  constructor(
    private router: Router,
    public mediaService: MediaService,
    public selectionService: SelectionService
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

    this.searchSubject.pipe(
      debounceTime(300),
      distinctUntilChanged(),
      switchMap(query => {
        if (query.length > 3) {
          return this.mediaService.searchMedia(query);
        } else if (query.length === 0) {
          return this.mediaService.getAllMedia();
        } else {
          return of([]);
        }
      })
    ).subscribe(results => {
      this.filteredMediaList = results;
    });

    this.loadMedia();
  }

  loadMedia() {
    this.mediaService.getAllMedia().subscribe(media => {
      this.filteredMediaList = media;
    });
  }

  onSearchChange(query: string) {
    this.searchSubject.next(query);
  }

  createMovie() {
    this.router.navigate(['admin/create-movie']);
  }

  createSeries() {
    this.router.navigate(['admin/create-series']);
  }

  editMedia(media: MediaCardModel) {
    const url = media.type === 'SERIES'
      ? `/admin/edit-series/${media.id}`
      : `/admin/edit-movie/${media.id}`;
    console.log('Navigating to edit:', media);
    this.router.navigateByUrl(url, { state: { media } });
  }

  showDeleteConfirmation(media: MediaCardModel) {
    this.mediaToDelete = media;
    this.confirmDeleteVisible = true;
  }

  confirmDelete(confirmed: boolean) {
    if (!confirmed || !this.mediaToDelete) {
      this.toast.showWarning('Deletion cancelled');
      this.confirmDeleteVisible = false;
      this.mediaToDelete = null;
      return;
    }

    const type = this.mediaToDelete.type.toUpperCase() as 'MOVIE' | 'SERIES';

    this.mediaService.deleteMedia(this.mediaToDelete.id, type).subscribe(() => {
      this.toast.showSuccess('Media deleted successfully!');
      this.confirmDeleteVisible = false;
      this.mediaToDelete = null;
      this.loadMedia();
    });
  }

  toggleSelected(media: MediaCardModel) {
    this.selectionService.toggleSelection(media.id);
  }
}
