import {Component, ElementRef, HostListener, Input, OnInit, ViewChild} from '@angular/core';
import {
  FormArray,
  FormBuilder,
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators
} from "@angular/forms";
import {CommonModule} from "@angular/common";
import {ActivatedRoute, Router} from '@angular/router';
import { MediaService } from '../../../../core/services/media.service';

@Component({
  selector: 'app-form-movie',
  standalone: true,
    imports: [
      CommonModule,
      ReactiveFormsModule,
    ],
  templateUrl: './form-movie.component.html',
  styleUrl: '../form-style.scss'
})
export class FormMovieComponent implements OnInit{
  @Input() mode: 'create' | 'edit' = 'create';
  @Input() movieData: any = null;

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
    private router: Router,
    private route: ActivatedRoute,
    private mediaService: MediaService
  ) {}

  ngOnInit() {
    this.initForm();
    this.id = Number(this.route.snapshot.paramMap.get('id'));

    if (this.id) {
      this.isEditMode = true;
      this.mediaService.getMediaById(this.id).subscribe(media => {
        this.populateForm(media);
      });
    } else {
      this.addVideo();
    }
  }

  initForm() {
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
      videos: this.fb.array([])
    });
  }

  populateForm(media: any) {
    this.form.patchValue({
      title: media.title,
      description: media.description,
      releaseDate: media.releaseDate,
      genres: media.genres || [],
      trailerUrl: media.trailerUrl,
      posterImg: media.posterImg,
      coverImg: media.coverImg,
      globalRating: media.globalRating
    });

    media.actors?.forEach((actor: string) => this.actors.push(new FormControl(actor)));
    media.directors?.forEach((director: string) => this.directors.push(new FormControl(director)));
    media.countries?.forEach((country: string) => this.countries.push(new FormControl(country)));
    media.videos?.forEach((video: any) => {
      this.videos.push(this.fb.group({
        resolution: video.resolution,
        url: video.url
      }));
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

  get videos(): FormArray {
    return this.form.get('videos') as FormArray;
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

  addVideo() {
    this.videos.push(this.fb.group({
      resolution: '',
      url: ''
    }));
  }

  removeVideo(index: number) {
    this.videos.removeAt(index);
  }

  getControl(array: FormArray, index: number): FormControl {
    return array.at(index) as FormControl;
  }

  submit() {
    const payload = {
      ...this.form.value,
      genres: this.selectedGenreValues,
      type: 'Movie',
    };

    if (this.isEditMode) {
      this.mediaService.updateMedia(this.id, payload).subscribe(() => {
        this.router.navigate(['/admin'], {
          state: { showEditMovieToast: true }
        });
      });
    } else {
      this.mediaService.createMedia(payload).subscribe(() => {
        this.router.navigate(['/admin'], {
          state: { showMovieToast: true }
        });
      });
    }
  }
}
