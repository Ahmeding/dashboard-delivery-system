import { format } from 'date-fns';

export const formatDateTimeDay = (date: Date): string => format(date, 'yyyy-MM-ddThh:mm');
export const formatTime = (date: Date): string => format(date, 'hh:mm');
export const formatDateDay = (date: Date): string => format(date, 'yyyy-MM-dd');
export const formatDate = (date: Date): string => format(date, 'MMM dd');
export const formatDayName = (date: Date): string => format(date, 'EEEE');

export const getDestinationColor = (destination: string): string => {
  // Create a consistent color based on the destination string
  const colors = {
    'Vienne': '#3498db',
    'Lyon': '#2ecc71',
    'Marseille': '#e74c3c',
    'Bordeaux': '#f1c40f',
    'Lille': '#9b59b6',
    // Add more cities and their colors
  };

  return colors[destination as keyof typeof colors] || '#3498db';
};

export const getMissionStatusIcon = (status: string): string => {
  const icons = {
    'EN_COURS': 'bi-play-circle-fill text-primary',
    'ACCOMPLI': 'bi-check-circle-fill text-success',
    'ANNULEE': 'bi-x-circle-fill text-danger',
    'TERMINIEE': 'bi-flag-fill text-info'
  };
  return icons[status as keyof typeof icons] || 'bi-question-circle-fill';
};
