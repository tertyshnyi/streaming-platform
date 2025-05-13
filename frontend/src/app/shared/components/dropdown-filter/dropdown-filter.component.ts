import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-dropdown',
  standalone: true,
  imports: [ CommonModule ],
  templateUrl: './dropdown-filter.component.html',
  styleUrls: ['./dropdown-filter.component.scss'],
})
export class DropdownComponent {
  @Input() label: string = '';
  @Input() items: string[] = [];
  @Input() selectedItems: string[] = [];
  @Input() isOpen: boolean = false;
  @Input() isMultiple: boolean = false;
  @Output() toggleDropdown = new EventEmitter<void>();
  @Output() selectionChanged = new EventEmitter<string[]>();

  onItemSelect(item: string) {
    if (this.isMultiple) {
      const updatedSelection = this.selectedItems.includes(item)
        ? this.selectedItems.filter((i) => i !== item)
        : [...this.selectedItems, item];
      this.selectionChanged.emit(updatedSelection);
    } else {
      this.selectionChanged.emit([item]);
    }
  }
}
