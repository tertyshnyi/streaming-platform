import {
  ChangeDetectorRef,
  Component,
  ElementRef,
  HostListener,
  Input,
  OnInit,
  ViewChild
} from '@angular/core';
import {
  AbstractControl,
  FormArray,
  FormBuilder,
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators
} from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { MediaService } from '../../../../core/services/media.service';
import { SeriesModel } from '../../../../core/models/series.model';

@Component({
  selector: 'app-form-series',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule
  ],
  templateUrl: './form-series.component.html',
  styleUrls: ['../form-style.scss']
})
export class FormSeriesComponent implements OnInit {
  @Input() mode: 'create' | 'edit' = 'create';
  @Input() seriesData: any = null;

  @ViewChild('dropdownRef') dropdownRef!: ElementRef;
  genreDropdownOpen = false;

  form!: FormGroup;
  isEditMode = false;
  id!: number;

  genres = [
    'ACTION', 'ADVENTURE', 'ANIMATION', 'BIOGRAPHY', 'COMEDY', 'CRIME', 'DOCUMENTARY',
    'DRAMA', 'FAMILY', 'FANTASY', 'HISTORY', 'HORROR', 'MUSIC', 'MYSTERY', 'ROMANCE',
    'SCIENCE_FICTION', 'SPORT', 'THRILLER', 'WAR', 'WESTERN', 'MUSICAL', 'SUPERHERO',
    'SHORT', 'REALITY', 'TALK_SHOW', 'GAME_SHOW', 'NEWS', 'NOIR', 'EXPERIMENTAL'
  ];

  constructor(
    private fb: FormBuilder,
    private cdr: ChangeDetectorRef,
    private router: Router,
    private route: ActivatedRoute,
    private mediaService: MediaService
  ) {
    this.initForm();
  }

  ngOnInit() {
    this.id = Number(this.route.snapshot.paramMap.get('id'));
    if (this.id) {
      this.isEditMode = true;
      this.mediaService.getSeriesById(this.id).subscribe((media: SeriesModel) => {
        this.populateForm(media);
      });
    } else {
      this.addEpisode();
    }
  }

  private initForm() {
    this.form = this.fb.group({
      title: ['', Validators.required],
      description: [''],
      releaseDate: ['', Validators.required],
      genres: this.fb.control([]),
      actors: this.fb.array([]),
      directors: this.fb.array([]),
      countries: this.fb.array([]),
      trailerUrl: [''],
      posterImg: [''],
      coverImg: [''],
      globalRating: [null],
      episodes: this.fb.array([])
    });
  }

  populateForm(media: any) {
    this.form.patchValue({
      title: media.title,
      description: media.description,
      releaseDate: media.releaseDate ? new Date(media.releaseDate).toISOString().substring(0, 10) : '',
      genres: media.genres || [],
      trailerUrl: media.trailerUrl,
      posterImg: media.posterImg,
      coverImg: media.coverImg,
      globalRating: media.globalRating
    });

    media.actors?.forEach((actor: string) => this.actors.push(new FormControl(actor)));
    media.directors?.forEach((director: string) => this.directors.push(new FormControl(director)));
    media.countries?.forEach((country: string) => this.countries.push(new FormControl(country)));

    media.episodes?.forEach((ep: any) => {
      const episodeGroup = this.fb.group({
        title: [ep.title],
        duration: [ep.duration, Validators.required],
        videos: this.fb.array([])
      });

      ep.videos?.forEach((video: any) => {
        this.getVideosControls(episodeGroup).push(this.fb.group({
          resolution: [video.resolution],
          url: [video.url]
        }));
      });

      this.episodes.push(episodeGroup);
    });
  }

  @HostListener('document:click', ['$event'])
  onClickOutside(event: MouseEvent) {
    if (this.genreDropdownOpen && this.dropdownRef && !this.dropdownRef.nativeElement.contains(event.target)) {
      this.genreDropdownOpen = false;
    }
  }

  toggleGenreDropdown() {
    this.genreDropdownOpen = !this.genreDropdownOpen;
  }

  get selectedGenreValues(): string[] {
    return this.form.get('genres')?.value || [];
  }

  formattedGenres = this.genres.map(g => ({
    raw: g,
    label: g.replace(/_/g, ' ').toLowerCase()
  }));

  get formattedSelectedGenres(): string[] {
    return this.selectedGenreValues.map(g =>
      this.capitalizeFirst(g.replace(/_/g, ' ').toLowerCase())
    );
  }

  onGenreChange(event: Event) {
    const input = event.target as HTMLInputElement;
    const selected = this.selectedGenreValues;

    if (input.checked) {
      this.form.get('genres')?.setValue([...selected, input.value]);
    } else {
      this.form.get('genres')?.setValue(selected.filter(g => g !== input.value));
    }
  }

  private capitalizeFirst(str: string): string {
    return str.charAt(0).toUpperCase() + str.slice(1);
  }

  get actors(): FormArray {
    return this.form.get('actors') as FormArray;
  }

  get directors(): FormArray {
    return this.form.get('directors') as FormArray;
  }

  get countries(): FormArray {
    return this.form.get('countries') as FormArray;
  }

  get episodes(): FormArray {
    return this.form.get('episodes') as FormArray;
  }

  addActor() {
    this.actors.push(new FormControl(''));
  }

  removeActor(index: number) {
    this.actors.removeAt(index);
  }

  addDirector() {
    this.directors.push(new FormControl(''));
  }

  removeDirector(index: number) {
    this.directors.removeAt(index);
  }

  addCountry() {
    this.countries.push(new FormControl(''));
  }

  removeCountry(index: number) {
    this.countries.removeAt(index);
  }

  addEpisode() {
    this.episodes.push(this.fb.group({
      title: [''],
      duration: [null, Validators.required],
      videos: this.fb.array([])
    }));
    this.cdr.detectChanges();
  }

  removeEpisode(index: number) {
    this.episodes.removeAt(index);
  }

  addVideo(episodeIndex: number) {
    const episode = this.episodes.at(episodeIndex) as FormGroup;
    const videos = episode.get('videos') as FormArray;
    videos.push(this.fb.group({
      resolution: [''],
      url: ['']
    }));
  }

  removeVideo(episodeIndex: number, videoIndex: number) {
    const videos = this.getVideosControls(this.episodes.at(episodeIndex));
    videos.removeAt(videoIndex);
  }

  getControl(array: FormArray, index: number): FormControl {
    return array.at(index) as FormControl;
  }

  getVideosControls(ep: AbstractControl): FormArray {
    return ep.get('videos') as FormArray;
  }

  submit() {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const payload = {
      ...this.form.value,
      genres: this.selectedGenreValues,
      type: 'SERIES'
    };

    if (this.isEditMode) {
      this.mediaService.updateSeries(this.id, payload).subscribe(() => {
        this.router.navigate(['/admin'], {
          state: { showEditSeriesToast: true }
        });
      });
    } else {
      this.mediaService.createSeries(payload).subscribe(() => {
        this.router.navigate(['/admin'], {
          state: { showSeriesToast: true }
        });
      });
    }
  }
}
